package com.example.budgeterapp

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.DatePicker
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_edit_profile.*
import java.util.*

class EditProfileActivity : AppCompatActivity()  {

    lateinit var phone:String
    private val PICK_IMAGE_REQUEST = 1
    private lateinit var mImageUri: Uri

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        phone=intent.getSerializableExtra("key") as String

        save_EditProfile.setOnClickListener {
            update_profile()
            val intent = Intent(applicationContext,ProfileActivity::class.java)
            intent.putExtra("key",phone)
            startActivity(intent)
        }

        edit_profile_choose.setOnClickListener {
            imageChooser()
        }

        edit_profile_upload.setOnClickListener {
            imageUploader()
        }

        date_edit.setOnClickListener {

            val c= Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)
            val dpd = DatePickerDialog(this,
                DatePickerDialog.OnDateSetListener{ _: DatePicker?, myear: Int, mmonth: Int, dayOfMonth: Int ->
                    var months = mmonth
                    months+=1
                    edit_profile_birthday.setText("$dayOfMonth/$months/$myear")
                },year,month,day)

            dpd.show()
        }

        val ref = FirebaseDatabase.getInstance().getReference("User")
        ref.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(p0: DataSnapshot) {
                for(h in p0.children)
                {
                    val value = h.getValue(User::class.java)
                    if(value?.phone == phone){
                        edit_profile_name.hint = value.name
                        edit_profile_email.hint = value.email
                        edit_profile_password.hint = value.pass
                        edit_profile_birthday.hint = value.bod
                        edit_profile_phone.text = value.phone
                    }
                }
            }
        })

        val ref2 = FirebaseDatabase.getInstance().getReference("uploads")
        ref2.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }
            override fun onDataChange(p0: DataSnapshot) {
                for(h in p0.children)
                {
                    val value = h.getValue(Upload::class.java)
                    if(value!!.phone == phone){
                        if(value.mImageUrl == ""){
                            Toast.makeText(applicationContext,"Error", Toast.LENGTH_SHORT).show()
                        }
                        else {
                            Picasso.with(applicationContext).load(value.mImageUrl).into(edit_profile_img)
                        }
                    }
                }

            }
        })
    }

    private fun imageChooser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    private fun imageUploader() {
        val storage = FirebaseStorage.getInstance().getReference("uploads")
        val ref = FirebaseDatabase.getInstance().getReference("uploads")

        val file  = storage.child("image"+ mImageUri.lastPathSegment)

         file.putFile(mImageUri).addOnSuccessListener {

             file.downloadUrl.addOnSuccessListener { it2 ->
                 val upload = Upload(phone, it2.toString())
                 ref.child(phone).setValue(upload).addOnCompleteListener {
                     Toast.makeText(this,"Image Uploaded",Toast.LENGTH_SHORT).show()
                 }
             }

        }.addOnFailureListener {
            Toast.makeText(this,"Failure",Toast.LENGTH_SHORT).show()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK
            && data != null && data.data !=null){
            mImageUri = data.data!!
            edit_profile_img.setImageURI(mImageUri)
        }
    }

    private fun update_profile()
    {
        val ref = FirebaseDatabase.getInstance().getReference("User")
        var email = edit_profile_email.text.toString()

        var date = edit_profile_birthday.text.toString()
        var pass = edit_profile_password.text.toString()
        var name = edit_profile_name.text.toString()


        if(email.isEmpty())
            email = edit_profile_email.hint.toString()
        if(name.isEmpty())
            name = edit_profile_name.hint.toString()
        if(pass.isEmpty())
            pass = edit_profile_password.hint.toString()

        if(date.isEmpty())
            date = edit_profile_birthday.hint.toString()

        val user=User(name,email,pass,phone,date)

        ref.child(phone).setValue(user)
    }
}
