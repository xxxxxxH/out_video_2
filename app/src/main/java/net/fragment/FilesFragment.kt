package net.fragment

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.tencent.mmkv.MMKV
import kotlinx.android.synthetic.main.layout_fragment_files.*
import net.adapter.FolderAdapter
import net.basicmodel.FolderActivity
import net.basicmodel.R
import net.entity.FolderEntity
import net.entity.VideoEntity
import net.entity.VideoEntityWithoutBitmap
import net.event.MessageEvent
import net.utils.Contanst
import net.utils.LoadingDialog
import net.utils.Utils
import net.utils.VideoManager
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class FilesFragment : Fragment() {

    var dialog: LoadingDialog? = null
    var folderAdapter: FolderAdapter? = null
    var data: ArrayList<FolderEntity>? = null
    var map: HashMap<String, ArrayList<VideoEntity>> = HashMap()
    var videos: ArrayList<VideoEntity>? = null
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
    fun onEvent(event:MessageEvent){
        val msg = event.getMessage()[0]
        if (!TextUtils.isEmpty(msg)){
            initData()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    override fun onResume() {
        super.onResume()
        videos = VideoManager.get().getAllVideos(requireActivity())
        map = VideoManager.get().getMap()
        data = VideoManager.get().getFolderVideos(videos!!)
    }

    private fun initData() {
        showDlg()
        videos = VideoManager.get().getAllVideos(requireActivity())
        map = VideoManager.get().getMap()
        data = VideoManager.get().getFolderVideos(videos!!)
        initView()
        closeDlg()
    }

    private fun initView(){
        folderAdapter = FolderAdapter(requireActivity(),R.layout.layout_item,data)
        recycler.layoutManager = LinearLayoutManager(activity)
        recycler.adapter = folderAdapter
        folderAdapter!!.setOnItemClickListener(object : OnItemClickListener{
            override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
               val name = (adapter.data[position] as FolderEntity).name
                val videoEntity = VideoManager.get().getMap()[name] as ArrayList<VideoEntity>
                val data:ArrayList<VideoEntityWithoutBitmap> = Utils.handleEntityBitmap(videoEntity)
                Contanst.data.clear()
                Contanst.data = videoEntity
                val i = Intent(activity,FolderActivity::class.java)
                activity?.startActivity(i)
            }

        })
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