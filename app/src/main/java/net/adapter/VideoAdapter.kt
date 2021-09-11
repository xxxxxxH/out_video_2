package net.adapter

import android.app.Activity
import android.widget.ImageView
import android.widget.RelativeLayout
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import net.basicmodel.R
import net.entity.VideoEntity
import net.utils.ScreenUtils

class VideoAdapter(
    private val activity: Activity,
    layoutResId: Int,
    data: ArrayList<VideoEntity>?
) :
    BaseQuickAdapter<VideoEntity, BaseViewHolder>(layoutResId, data) {
    override fun convert(holder: BaseViewHolder, item: VideoEntity) {
        holder.setGone(R.id.menu, false)
        val imageView = holder.getView<ImageView>(R.id.item_img)
        val imgRoot = holder.getView<RelativeLayout>(R.id.item_img_root)
        val p = imgRoot.layoutParams
        p.height = ScreenUtils.getScreenSize(activity)[0] / 5
        p.width = ScreenUtils.getScreenSize(activity)[1] / 3
        imgRoot.layoutParams = p
        Glide.with(activity).load(item.url).into(imageView)
        holder.setText(R.id.item_tv_1, item.name)
        holder.setText(R.id.item_tv_2, item.duration)
    }
}