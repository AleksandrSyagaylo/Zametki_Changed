package com.example.myzametki1

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore


class read : AppCompatActivity() {
    //Метод onCreate вызывается при создании активности
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_read)
        //Обработка нажатия на кнопку "Назад"
        var back:TextView = findViewById(R.id.back)
        back.setOnClickListener {
            startActivity(Intent(this,MainActivity::class.java))
        }
        //Инициализация текстовых полей
        var textmain:TextView = findViewById(R.id.textmain)
        var textdesc : TextView = findViewById(R.id.textdesc)
        //получения данных из базы данных
        val id = intent.getStringExtra("id").toString()
        //Работа с Firebase Firestore
        var db = Firebase.firestore
        db.collection("reports").document(id).get().addOnSuccessListener {
            textmain.text = it.getString("name").toString()
            textdesc.text = it.getString("desktext").toString()
        }.addOnFailureListener {
            Toast.makeText(this,"Не удалось получить данные!", Toast.LENGTH_LONG).show()
        }


    }
}