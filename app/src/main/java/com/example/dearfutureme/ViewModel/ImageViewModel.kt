package com.example.dearfutureme.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dearfutureme.Model.Image

class ImageViewModel: ViewModel() {
    private val _images = MutableLiveData<List<Image>>()
    val images: LiveData<List<Image>> get() = _images

    fun updateImages(newImage: Image) {
        val currentImages = _images.value ?: emptyList()
        _images.value = currentImages + newImage
    }
}