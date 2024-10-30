package com.moviepedia.app.view

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.moviepedia.app.App
import com.moviepedia.app.BuildConfig
import com.moviepedia.app.R
import com.moviepedia.app.databinding.FragmentEditItemBinding
import com.moviepedia.app.model.Movie
import com.moviepedia.app.model.Movie.Companion.toMovie
import com.moviepedia.app.utils.FileUtils
import com.moviepedia.app.utils.PermissionUtil
import com.moviepedia.app.viewmodel.MovieViewModel
import com.moviepedia.app.viewmodel.MovieViewModelFactory
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


class EditItemFragment : Fragment() {

    private lateinit var binding:FragmentEditItemBinding
    private var movie:Movie?=null
    private var currentPhotoPath:File? = null

    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) {
        val galleryUri = it
        try{
            val copiedFilePath = FileUtils.copyUriToAppStorage(requireActivity(),galleryUri!!)
            currentPhotoPath = File(copiedFilePath!!)
            binding.movieSelectedImage.setImageURI(currentPhotoPath!!.toUri())
        }catch(e:Exception){
            e.printStackTrace()
        }

    }

    private val movieViewModel: MovieViewModel by viewModels {
        MovieViewModelFactory((requireActivity().application as App).repository)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentEditItemBinding.inflate(inflater, container, false)

        movie = arguments?.toMovie()
        if (movie != null) {
            binding.titleEditText.setText(movie?.title)
            binding.descriptionEditText.setText(movie?.description)
            binding.genreEditText.setText(movie?.genre)
            binding.releaseDateEditText.setText(movie?.releaseDate)
            binding.ratingEditText.setText("${movie?.rating}")
            if (movie?.photoUri!!.isNotEmpty()){
                currentPhotoPath = File(movie?.photoUri!!)
                if (currentPhotoPath!!.exists()) {
                    val myBitmap = BitmapFactory.decodeFile(currentPhotoPath!!.absolutePath)
                    binding.movieSelectedImage.setImageBitmap(myBitmap)
                }
            }
        }

        binding.releaseDateEditText.setOnClickListener {
            showDatePickerDialog()
        }

        binding.updateBtn.setOnClickListener {
            val title = binding.titleEditText.text.toString()
            val description = binding.descriptionEditText.text.toString()
            val genre = binding.genreEditText.text.toString()
            val releaseDate = binding.releaseDateEditText.text.toString()
            val rating = binding.ratingEditText.text.toString().toFloatOrNull() ?: 0f

            if (title.isBlank() || description.isBlank() || genre.isBlank() || releaseDate.isBlank() || rating <= 0f) {
                Toast.makeText(requireContext(), R.string.empty_input_warning_message, Toast.LENGTH_SHORT).show()
            } else {
                movie?.title = title
                movie?.description = description
                movie?.genre = genre
                movie?.releaseDate = releaseDate
                movie?.rating = rating
                movie?.photoUri = currentPhotoPath!!.absolutePath
                movieViewModel.update(movie!!)
                Toast.makeText(requireContext(), R.string.updated_movie_message, Toast.LENGTH_SHORT).show()
                findNavController().navigateUp()
            }
        }

        binding.uploadMovieImageBtn.setOnClickListener {
            showImagePickerDialog()
        }
        return binding.root
    }

    private fun openGallery() {
        galleryLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun showImagePickerDialog() {
        val options = resources.getStringArray(R.array.choose_options)

        val builder = MaterialAlertDialogBuilder(requireActivity())
        builder.setTitle(R.string.choose_an_option)
        builder.setItems(options) { dialog, which ->
            when (which) {
                0 -> {
                    handleCameraPermission()//dispatchTakePictureIntent()
                }
                1 -> {
                    openGallery()
                }
            }
        }
        builder.show()
    }

    private fun handleCameraPermission() {
        when {
            ContextCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                dispatchTakePictureIntent()
            }

            else -> {
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                dispatchTakePictureIntent()
            } else {
                MaterialAlertDialogBuilder(requireActivity())
                    .setTitle(R.string.permission_alert_message)
                    .setMessage(R.string.camera_permission_denied_message)
                    .setCancelable(false)
                    .setPositiveButton(R.string.setting){ dialog,which->
                        dialog.dismiss()
                        PermissionUtil.openAppSettings(requireActivity())
                    }
                    .setNegativeButton(R.string.cancel){dialog,which->
                        dialog.dismiss()
                    }
                    .show()
            }
        }

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
//                val imgFile = File(selectedPhotoUri)
                if (currentPhotoPath!!.exists()) {
                    val myBitmap = BitmapFactory.decodeFile(currentPhotoPath!!.absolutePath)
                    binding.movieSelectedImage.setImageBitmap(myBitmap)
                }
            }
        }

    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            currentPhotoPath = createImageFile()
            val uri = FileProvider.getUriForFile(requireActivity(), "${BuildConfig.APPLICATION_ID}.fileprovider", currentPhotoPath!!)
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
            resultLauncher.launch(takePictureIntent)
        } catch (e: ActivityNotFoundException) {
            Log.d("TEST1999",e.localizedMessage!!)
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        ).apply {
        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
            val selectedDate = Calendar.getInstance()
            selectedDate.set(selectedYear, selectedMonth, selectedDay)
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            binding.releaseDateEditText.setText(dateFormat.format(selectedDate.time))
        }, year, month, day)

        datePickerDialog.show()
    }
}