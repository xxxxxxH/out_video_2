package net.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemChildClickListener
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.tencent.mmkv.MMKV
import com.zxy.zxydialog.AlertDialogUtils
import kotlinx.android.synthetic.main.layout_fragment_files.*
import net.adapter.VideoAdapter
import net.basicmodel.R
import net.basicmodel.VideoActivity
import net.entity.VideoEntity
import net.entity.VideoEntityWithoutBitmap
import net.event.MessageEvent
import net.utils.LoadingDialog
import net.utils.Utils
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class HistoryFragment : Fragment() {

    var data: ArrayList<VideoEntity>? = null
    var videoAdapter: VideoAdapter? = null
    var current: Int = 0
    var currentName :String?=null
    var dialog: LoadingDialog? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.layout_fragment_files, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        EventBus.getDefault().register(this)
        initData()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: MessageEvent) {
        val msg = event.getMessage()[0]
        if (!TextUtils.isEmpty(msg)) {
            initData()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    private fun initData() {
        showDlg()
        val key = MMKV.defaultMMKV()!!.decodeStringSet("key") as HashSet<String>
        if (key != null) {
            val temp = ArrayList<VideoEntityWithoutBitmap>()
            for (item in key) {
                val entity = MMKV.defaultMMKV()!!
                    .decodeParcelable(item, VideoEntityWithoutBitmap::class.java)
                if (entity != null) {
                    temp.add(entity)
                }
            }
            data = Utils.returnData(temp)
        }
        initView()
        closeDlg()
    }

    private fun initView() {
        videoAdapter = VideoAdapter(requireActivity(), R.layout.layout_item, data)
        recycler.layoutManager = LinearLayoutManager(activity)
        recycler.adapter = videoAdapter
        videoAdapter!!.addChildClickViewIds(R.id.menu)
        videoAdapter!!.setOnItemChildClickListener(object : OnItemChildClickListener {
            override fun onItemChildClick(
                adapter: BaseQuickAdapter<*, *>,
                view: View,
                position: Int
            ) {
                if (view.id == R.id.menu) {
                    showMenu(adapter.data[position] as VideoEntity)
                }
                current = position
                currentName = (adapter.data[position] as VideoEntity).name
            }

        })
        videoAdapter!!.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
                val entity = adapter.data[position] as VideoEntity
                val url = entity.url
                val name = entity.name
                val intent = Intent(activity, VideoActivity::class.java)
                intent.putExtra("url", url)
                intent.putExtra("name", name)
                Utils.saveKey(name)
                MMKV.defaultMMKV()?.encode(name, Utils.handleSingleEntityBitmap(entity))
                startActivity(intent)
            }
        })
    }

    private fun showMenu(data: VideoEntity) {
        AlertDialogUtils.build(requireActivity())
            .setView(R.layout.layout_dialog_pop)
            .setTransparency(0.5f)
            .setOnClick(R.id.play, R.id.information, R.id.delete)
            .create { view, alertDialogUtils ->
                when (view.id) {
                    R.id.play -> {
                        alertDialogUtils.dismiss()
                    }
                    R.id.information -> {
                        showInfoDlg(data).show()
                        alertDialogUtils.dismiss()
                    }
                    R.id.delete -> {
                        alertDialogUtils.dismiss()
                        videoAdapter!!.removeAt(current)
                        MMKV.defaultMMKV()!!.remove(currentName)
                    }
                }
            }
    }

    @SuppressLint("SetTextI18n")
    private fun showInfoDlg(data: VideoEntity): AlertDialog {
        val d = AlertDialog.Builder(requireActivity()).create()
        val view = LayoutInflater.from(requireActivity()).inflate(R.layout.layout_dialog_info, null)
        d.setView(view)
        view.findViewById<TextView>(R.id.filename).text = "FileName:${data.name}"
        view.findViewById<TextView>(R.id.filepath).text = "FilePath:${data.url}"
        view.findViewById<TextView>(R.id.duration).text = "Duration:${data.duration}"
        view.findViewById<TextView>(R.id.type).text =
            "Type:${data.url.substring(data.url.indexOfLast { it == '.' } + 1)}"
        view.findViewById<TextView>(R.id.confirm).setOnClickListener {
            d.dismiss()
        }
        return d
    }

    private fun showDlg() {
        dialog?.show()?.run {
            dialog = LoadingDialog(activity)
            dialog!!.show()
        }
    }

    private fun closeDlg() {
        dialog?.dismiss()
    }
}