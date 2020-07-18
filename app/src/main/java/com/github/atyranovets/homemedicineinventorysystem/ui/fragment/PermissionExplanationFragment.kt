package com.github.atyranovets.homemedicineinventorysystem.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.github.atyranovets.homemedicineinventorysystem.R
import com.github.atyranovets.homemedicineinventorysystem.lib.Constants
import com.github.atyranovets.homemedicineinventorysystem.lib.PermissionExplanation
import com.github.atyranovets.homemedicineinventorysystem.lib.PermissionRequirementInfo

class PermissionExplanationFragment : Fragment() {

    private var info: PermissionRequirementInfo? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            it.getString(Constants.FragmentArgs.PermissionExplanation.permissionId)?.let {
                info = PermissionExplanation.permissionExplanations[it];
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_permission_explanation, container, false);
        view?.let {
            it.findViewById<TextView>(R.id.permissionExplainTitle).text = getString(info!!.title);
            it.findViewById<ImageView>(R.id.permissionExplainIcon).apply {
                setImageResource(info!!.icon);
                contentDescription = getString(info!!.title);
            }
            it.findViewById<TextView>(R.id.permissionExplainDescription).text = getString(info!!.info);
        }
        return view.rootView;
    }
}
