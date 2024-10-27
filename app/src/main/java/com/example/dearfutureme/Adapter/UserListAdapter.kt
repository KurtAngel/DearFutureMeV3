import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.dearfutureme.R

class UserListAdapter(context: Context, private val userList: List<String>) :
    ArrayAdapter<String>(context, 0, userList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.user_list_item, parent, false)

        val userItemText = view.findViewById<TextView>(R.id.userItemText)

        userItemText.text = userList[position]

        return view
    }
}
