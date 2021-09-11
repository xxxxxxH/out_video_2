package net.basicmodel

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import kotlinx.android.synthetic.main.layout_bottom.*
import kotlinx.android.synthetic.main.layout_top.*
import net.event.MessageEvent
import net.fragment.FilesFragment
import net.fragment.HistoryFragment
import org.greenrobot.eventbus.EventBus

class HomeActivity : AppCompatActivity() {

    var filesFragment: FilesFragment? = null
    var historyFragment: HistoryFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_activity_home)
        showPosition(0)
        initView()
    }

    private fun showPosition(position: Int) {
        val fm = supportFragmentManager
        val ft = fm.beginTransaction()
        hideAll(ft)
        if (position == 0) {
            filesFragment = fm.findFragmentByTag("files") as FilesFragment?
            if (filesFragment == null) {
                filesFragment = FilesFragment()
                ft.add(R.id.content, filesFragment!!, "files")
            } else {
                ft.show(filesFragment!!)
            }
            if (historyFragment != null) {
                ft.remove(historyFragment!!)
            }

        } else {
            historyFragment = fm.findFragmentByTag("history") as HistoryFragment?
            if (historyFragment == null) {
                historyFragment = HistoryFragment()
                ft.add(R.id.content, historyFragment!!, "history")
            } else {
                ft.show(historyFragment!!)
            }
        }
        ft.commit()
    }

    private fun hideAll(ft: FragmentTransaction) {
        filesFragment?.let {
            ft.hide(filesFragment!!)
        }
        historyFragment?.let {
            ft.hide(historyFragment!!)
        }
    }

    private fun initView() {
        folder.setOnClickListener {
            showPosition(0)
        }
        history.setOnClickListener {
            showPosition(1)
        }
        refresh.setOnClickListener {
            EventBus.getDefault().post(MessageEvent("f"))
        }
    }
}