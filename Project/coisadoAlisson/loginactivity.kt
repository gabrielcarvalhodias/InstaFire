pacakage edu.stanford.rkpandey.instafire

import ...

priveate const val TAG = "LoginActivity"
class LoginActivity : AppCompatActivity() {

    override fun onCriate (savedInstanceState : Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.activity_login)
        val auth = FirebaseAuth.getInstance()
        if (auth.currentUser != null){
            goPostActivity()
        }

        btnLogin.setOnClickListener {
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()
            if (email.isBlank() || password.isBlank()){
                Toast.makeText ( context: this, text: "Email/password cannot be empty", Toast.LENGTH_SHORT).show()
            }

            auth.signInWithEmailAndPassword(email.password).addOnCompleteListener {task->
                btnLogin.isEnabled = true
                if (task.isSuccessful) {
                    Toast.makeText(text: this, text:"Successful", Toast.LENGTH_SHORT).show()
                    goPostActivity()
                }else{
                    Log.e(TAG, msg: "signInWithEmail failed", task.except)
                    Toast.makeText(context:this, text: "Authentication failed", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}