package com.example.externalstorage

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.*
import java.lang.Exception
import java.lang.StringBuilder

class MainActivity : AppCompatActivity() {

    val REQUEST_PERMISSION_CODE = 1

    lateinit var fileText: EditText
    lateinit var btnSave: Button
    lateinit var btnRead: Button
    lateinit var MyExternalFile: File
    lateinit var strData: String

    val file_name = "file Text.txt"
    val file_path = "MyFileStorage"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fileText = findViewById(R.id.fileEditText)
        btnSave = findViewById(R.id.saveButton)
        btnRead = findViewById(R.id.readButton)

        if (!checkPermissionFromDevice())
            requestPermission()

        if (checkPermissionFromDevice()) {
            btnSave.setOnClickListener {
                saveFile()
            }
        } else {
            requestPermission()
        }
        btnRead.setOnClickListener {
            readFile()
        }
    }

    private fun readFile() {
        try {
            val directory = File(Environment.getExternalStorageDirectory().toString() + "/TestData")
            directory.mkdir()
            val file = File(Environment.getExternalStorageDirectory().toString(), file_name)

            val fileInputStream = FileInputStream(file)

            val inputStreamRead = InputStreamReader(fileInputStream)
            val bufferReader = BufferedReader(inputStreamRead)
            val stringBuilder = StringBuilder()
            var line: String? = null

            while ({ line = bufferReader.readLine();line }() != null) {
                stringBuilder.append(line)
            }
            fileInputStream.close()
            inputStreamRead.close()
            fileText.setText(stringBuilder.toString())

            Toast.makeText(applicationContext, "Data Retrieved...", Toast.LENGTH_SHORT).show()
        } catch (exp: java.io.IOException) {
            exp.printStackTrace()
        }
    }

    private fun saveFile() {
        val directory = File(Environment.getExternalStorageDirectory().toString() + "/TestData")
        directory.mkdir()

        val file = File(Environment.getExternalStorageDirectory(), file_name)
        val fileOutputStream = FileOutputStream(file)
        strData = fileText.text.toString()
        fileOutputStream.write(strData.toByteArray())
        fileOutputStream.close()

        Toast.makeText(applicationContext, "Data Saved...", Toast.LENGTH_SHORT).show()
        fileText.setText("")
    }

    private fun checkPermissionFromDevice(): Boolean {
        val writeExternalStorage = ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        return writeExternalStorage == PackageManager.PERMISSION_GRANTED

    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
            REQUEST_PERMISSION_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_PERMISSION_CODE -> if (grantResults!!.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)

                Toast.makeText(applicationContext, "Permission Granted...", Toast.LENGTH_LONG)
                    .show()
        }

    }
}


