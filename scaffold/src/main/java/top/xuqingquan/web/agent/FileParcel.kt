package top.xuqingquan.web.agent

import android.os.Parcel
import android.os.Parcelable

data class FileParcel(var id: Int, var contentPath: String?, var fileBase64: String?) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(contentPath)
        parcel.writeString(fileBase64)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FileParcel> {
        override fun createFromParcel(parcel: Parcel): FileParcel {
            return FileParcel(parcel)
        }

        override fun newArray(size: Int): Array<FileParcel?> {
            return arrayOfNulls(size)
        }
    }

}
