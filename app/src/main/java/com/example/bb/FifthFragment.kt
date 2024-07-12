import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.bb.R
import com.example.bb.FourthFragment   // Import your FourthFragment class
import com.example.bb.SixthFragment

class FifthFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_fifth, container, false)
        

        // Assuming 'facultyButton' is a button in your fragment_fifth.xml layout
        val facultyButton = view.findViewById<Button>(R.id.facultyButton)
        facultyButton.setOnClickListener {
            // Replace FifthFragment with FourthFragment
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, FourthFragment())
                .addToBackStack(null)
                .commit()
        }

        val positionButton = view.findViewById<Button>(R.id.positionButton)
        positionButton.setOnClickListener {
            // Replace FifthFragment with SixthFragment
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, SixthFragment())
                .addToBackStack(null)
                .commit()
        }

        return view
    }
}
