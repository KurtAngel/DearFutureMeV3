package com.example.dearfutureme.Model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName


data class Image(
    val id: Int,
    @SerializedName("image_url")
    val imageUrl: String
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString().toString()
    )

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(p0: Parcel, p1: Int) {
        p0.writeInt(id)
        p0.writeString(imageUrl)
    }

    companion object CREATOR : Parcelable.Creator<Image> {
        override fun createFromParcel(parcel: Parcel): Image {
            return Image(parcel)
        }

        override fun newArray(size: Int): Array<Image?> {
            return arrayOfNulls(size)
        }
    }
}

