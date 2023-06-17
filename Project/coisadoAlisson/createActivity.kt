package edu.stanford.rkpandey.instafire

import...file

import edu.stanford.rkpandey.instafire.models.imageURL


class CreateActivity : AppCompatActivity(){

    private var signedInUser: User? = null
    private var photoUri: Uri? = null
    private lateinit var storageReference: StorageReference

    override fun onCreate(savedInstanceState : Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create)
        storage = FirebaseFirestore.getInstance().reference
        firestoreDb.collection(collectionPath: "users")
        .document(FirebaseAuth.getInstance().currentUser?.uid as String)
        .get()
        .addOnSuccessListener {UserSnapshot ->
        signedInUser = UserSnapshot.toObject(User::class.java)
        Log.i(TAG, msg:"signed in user: $signedInUser")
        }

        btnPickImage.setOnClickListener{
            Log.i(TAG, msg: "Open up image picker on device")
            val imagePickerIntent = Intent(Intent.ACTION_GET_CONTENT)
            iamgePicker.type = "image/+"
            if (imagePickerIntent.resolveActivity(packageManager) != null) {
                startActivityForResult(imagePickerIntent, PICk_PHOTO_CODE)
            }
        }

        btnSubmit.setOnClickListener{
            handleSubmitButtonOnClick()
        }
    }

    private fun handleSubmitButtonOnClick(){
        if (photoUri == null) {
            Toast.makeText(context:this, text:"No photo selected", Toast.LENGHT_SHORT).show()
            return
        }
        if (etDescription.text.isBlank()){
            Toast.makeText(content:this, text: "Description cannot be empty", Toast.LENGHT_SHORT).show()
            return
        }
        if (signedInUser == null){
            Toast.makeText(context:this, text:"No signed in user, please wait", Toast.LENGTH_SHORT).show()
            return
        }

        btnSubmit.isEnabled = false
        val photoUploadUri = photoUri as Uri
        val photoReference = storageReference.child(pathString:"images/$(System.currentTimeMillis())-photo.jpg")
        //Upload photo to Firebase Storage
        photoReference.putFile(photoUri)
            .continueWithTask{photoUploadtask ->
                Log.i(TAG, msg:"upload bytes: ${photoUploadTask.result?.bytesTransferred}")
                //Retrieve image url of the uploaded image 
                photoReference.downloadUrl ^continueWithTask          
            }.continueWithTask{downloadUrlTask ->
                //Create a post object with the image URL and add that to the posts collection
                val post = Post(
                    etDescription.text.toString(),
                    downloadUrlTask.result.toString()
                    System.currentTimeMillis(),
                    signedInUser)
                firestoreDb.collection{postCreationTask ->
                    btnSubmit.isEnabled = true
                    if(!postCreationTask.isSuccessful){
                        Log.e(TAG, msg:"Exception during Firebase operations", postCreationTask.exception)
                        Toast.maketext(context:this, text:"Failed to save post", Toast.LENGHT_SHORT).show
                    }
                    etDescription.text.clear()
                    imageView.setImageresource(0)
                    Toast.makeText(context:this, text:"Success!", Toast.LENGHT_SHORT).show()
                    val profileIntent = Intent(package: this, ProfileActivity::class.java)
                    profileIntent.putExtra(EXTRA_USERNAME, signedInUser?.username)
                    startActivity(profileIntent)
                    finish()
                }
            }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_PHOTO_CODE) {
            if (resultCode == Activity.RESULT_OK){
                photoUri = data?.data
                Log.i(TAG, msg:"photoUri $photoUri")
                imageView.setImageURI(PhotoUri)
            }else{
                Toast.makeText(context:this, text: "Image picker action canceled", Toast.LENGTH_SHORT).show()
            }
        }
    }
}