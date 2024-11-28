package com.example.myzametki1

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import java.util.Date

class addreport : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addreport)
        val back:TextView = findViewById(R.id.back)
        back.setOnClickListener {
            startActivity(Intent(this,MainActivity::class.java))
        }
        var db = Firebase.firestore
        var editextmain: EditText = findViewById(R.id.editextmain)
        var editextdesc: EditText = findViewById(R.id.editextdesc)
        var save:Button = findViewById(R.id.save)
        editextmain.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_NEXT || (event?.keyCode == KeyEvent.KEYCODE_ENTER && event.repeatCount == 0)) {
                editextdesc.requestFocus()
                 true
                }
                else{
                    false
                }
            }
        save.setOnClickListener {
            if (editextmain.text.isNotEmpty() && editextdesc.text.isNotEmpty()) {
                val currentDateMillis = Date().time // Получаем количество миллисекунд с начала эпохи

                val report = hashMapOf(
                    "date" to currentDateMillis, // Сохраняем как число (миллисекунды)
                    "desktext" to editextdesc.text.toString(),
                    "name" to editextmain.text.toString()
                )

                db.collection("reports")
                    .add(report)
                    .addOnSuccessListener { documentReference ->
                        finish() // Закрываем текущее активити
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Не удалось добавить!", Toast.LENGTH_LONG).show()
                        e.printStackTrace() // Добавлено для отладки ошибок
                    }
            } else {
                Toast.makeText(this, "Заполнены не все поля!", Toast.LENGTH_LONG).show()
            }
        }
    }
}