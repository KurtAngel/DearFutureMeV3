package com.example.dearfutureme.Model

import android.graphics.Bitmap
import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import org.w3c.dom.Text

data class Capsules(
    val id : Int,
    val title : String,
    val message : String,
    @SerializedName("receiver_email")
    val receiverEmail : String?,
    @SerializedName("scheduled_open_at")
    val scheduledOpenAt : String?,
    val draft : String?,
    val images: List<Image>
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        emptyList()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(title)
        parcel.writeString(message)
        parcel.writeString(receiverEmail)
        parcel.writeString(scheduledOpenAt)
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
