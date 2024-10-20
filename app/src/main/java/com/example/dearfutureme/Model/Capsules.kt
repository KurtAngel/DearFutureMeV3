package com.example.dearfutureme.Model

import android.graphics.Bitmap
import android.os.Parcel
import android.os.Parcelable
import org.w3c.dom.Text

data class Capsules(
    val id : Int,
    val title : String,
    val message : String,
    val content : String?,
    val receiver_email : String?,
    val schedule_open_at : String?,
    val draft : String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(title)
        parcel.writeString(message)
        parcel.writeString(content)
        parcel.writeString(receiver_email)
        parcel.writeString(schedule_open_at)
        parcel.writeString(draft)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Capsules> {
        override fun createFromParcel(parcel: Parcel): Capsules {
            return Capsules(parcel)
        }

        override fun newArray(size: Int): Array<Capsules?> {
            return arrayOfNulls(size)
        }
    }
}
