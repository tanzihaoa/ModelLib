package com.tzh.mylibrary.util.sound

import java.io.File

object FileUtils {

}

/**
 * 删除文件
 */
fun File?.delete() {
    this ?: return
    if (this.exists() && this.isFile) {
        delete()
    }
}

/**
 * 文件重命明
 */
fun File?.reName(newName: String?): File? {
    this ?: return null
    if (newName.isNullOrEmpty()) return null
    if (!this.exists()) return null
    val pFile = this.parentFile ?: return null
    val newFile = File(pFile.absolutePath + "/" + newName)
    if (newFile.exists()) {
        newFile.delete()
    }
    if (renameTo(newFile)) {
        return newFile
    }
    return null
}


