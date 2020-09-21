package vanphuc0497.job.mycv

import android.util.Log
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream

/**
 * Create by Nguyen Van Phuc on 8/18/20
 */
class Decompress(zipFile: String, location: String) {
    private val zip: String = zipFile
    private val loc: String = location

    init {
        dirChecker("")
    }

    fun unzip() {
        try {
            val fin = FileInputStream(zip)
            val zin = ZipInputStream(fin)
            var ze: ZipEntry? = null
            while (zin.nextEntry.also { ze = it } != null) {
                Log.e("xxx", "Unzipping " + ze?.name)

                if (ze?.isDirectory!!) {
                    dirChecker(ze?.name!!)
                } else {
                    val fout = FileOutputStream(loc + ze?.name)
                    var c: Int = zin.read()
                    while (c != -1) {
                        fout.write(c)
                        c = zin.read()
                    }
                    zin.closeEntry()
                    fout.close()
                }
            }
            zin.close()
        } catch (e: Exception) {
            Log.e("xxx", "unzip", e)
        }
    }

    private fun dirChecker(dir: String) {
        val f = File(loc)
        if (!f.isDirectory) {
            f.mkdirs()
        }
    }
}