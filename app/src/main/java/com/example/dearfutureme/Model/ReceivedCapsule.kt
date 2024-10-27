package com.example.dearfutureme.Model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class ReceivedCapsule(
    val id : Int,
    val title : String,
    val message : String,
    @SerializedName("receiver_email")
    val receiverEmail : String,
    @SerializedName("scheduled_open_at")
    val scheduledOpenAt : String,
    val images: List<Image>?,
    val sender: Sender
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        emptyList()
        ,Sender("","","")
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(title)
        parcel.writeString(message)
        parcel.writeString(receiverEmail)
        parcel.writeString(scheduledOpenAt)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ReceivedCapsule> {
        override fun createFromParcel(parcel: Parcel): ReceivedCapsule {
            return ReceivedCapsule(parcel)
        }

        override fun newArray(size: Int): Array<ReceivedCapsule?> {
            return arrayOfNulls(size)
        }
    }
}
