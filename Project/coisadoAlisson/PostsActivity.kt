package edu.stanford.rkpandey.instafire

import ...

import edu.stanford.rkpandey.instafire.CreateActivity







private const TAG = "PostsActivity"
const val EXTRA_USERNAME = "EXTRA_USERNAME"
open class PostsActivity : AppCompatActivity() {


    private var signedInUser: User? = null
    private lateinit var firestoreDb: FirebaseFirestore
    private lateinit var posts: MutableList<Post>
    private lateinit var adapter: PostsAdapter

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_posts)

        // Create the layout file which represents one posts
        // Create data source - DONE
        posts = mutableListOf()
        // Create the adapter
        adapter = PostsAdapter(context:this, posts)
        // Bind the adapter and layout manager to the RV
        rvPosts.adapter = adapter
        rvPosts.layoutManager = LinearLayoutManager(context:this)
        firestoreDb = FirebaseFirestore.getInstance()

        firestoreDb.collection(collectionPath: "users")
            .document(FirebaseAuth.getInstance().currentUser?.uid as String)
            .get()
            .addOnSuccessListener {UserSnapshot ->
            signedInUser = UserSnapshot.toObject(User::class.java)
            Log.i(TAG, msg:"signed in user: $signedInUser")
            }

        var postsReference = firestoreDb
            .collection(collection: "posts")
            .limit(limit:20)
            .orderBy(field:"creation_time_ms",Query.Direction.DESCENDING)

        val username = intent.getStringExtra(EXTRA_USERNAME)
        if (username != null){
            supportActionBar?.title = username
            postsReference = postsReference.whereEqualTo(field:"user.username", username)
        }

        val postsReference = firestoreDb.collection(collectionPath:"posts")
        postsReference.addSnapshotListener {snapshot, exception ->
            if (exception != null || snapshot == null) {
                Log.e(TAG, msg: "Exception when querying posts", exception)
                retun@addSnapshotListener
            }
            val postlist = snapshot.toObjects(Post::class.java)
            posts.clear()   
            posts.addAll(postList)
            adapter.notifyDataSetChanged()
            for (post in postList){
                Log.i(TAG, msg:"Post ${post}")
            }

            fabCreate.setOnClickListener{
                val intent = Intent(this, CreateActivity::class.java)
                startActivity(intent)
            }
        }

        //TODO: make a query to Firestore to retrieve data
    }

    override fun onCreateOptionsMenu(menu:Menu?):Boolean{
        menuInflater.inflate(R.menu.menu_posts, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item:MenuItem) : Boolean{
        if (item.itemId = R.id.menu_profile){
            val intent = Intent(packageContext:this, ProfileActivity::class.java)
            intent.putExtra(EXTRA_USERNAME, signedInUser?.username)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }
}