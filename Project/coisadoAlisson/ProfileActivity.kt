package edu.stanford.rkpandey.instafire

import...file

import sun.util.resources.Bundles





private const val TAG = "ProfileActivity"
class ProfileActivity : PostsActivity(){


    override fun onCreateOptionsMenu(menu: Menu?): Boolean{
        menuInflater.inflate(R.menu.menu_profile, menu)
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?):Boolean{
        if (item.itemId == R.id.menu_layout){
            Log.i(TAG, msg"Uer wants to logout")
            FirenaseAuth.getInstance().sigOut()
            startActivity(Intent(packageContext:this, LoginActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }
    
}