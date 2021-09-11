package net.adapter

import android.app.Activity
import android.widget.ImageView
import android.widget.RelativeLayout
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.othershe.combinebitmap.CombineBitmap
import com.othershe.combinebitmap.layout.WechatLayoutManager
import net.basicmodel.R
import net.entity.FolderEntity
import net.utils.ScreenUtils


class FolderAdapter(
    private val activity: Activity,
    layoutResId: Int,
    data: ArrayList<FolderEntity>?
) :
    BaseQuickAdapter<FolderEntity, BaseViewHolder>(layoutResId, data) {
    override fun convert(holder: BaseViewHolder, item: FolderEntity) {
        holder.setGone(R.id.menu,true)
        val imageView = holder.getView<ImageView>(R.id.item_img)
        val imgRoot = holder.getView<RelativeLayout>(R.id.item_img_root)
        val p = imgRoot.layoutParams
        p.height = ScreenUtils.getScreenSize(activity)[0] / 5
        p.width = ScreenUtils.getScreenSize(activity)[1] / 3
        imgRoot.layoutParams = p
        CombineBitmap.init(activity)
            .setLayoutManager(WechatLayoutManager())
            .setSize(ScreenUtils.getScreenSize(activity)[1] / 3)
            .setGap(2)
            .setBitmaps(*item.url)
            .setImageView(imageView)
            .build()
        holder.setText(R.id.item_tv_1, item.name)
        holder.setText(R.id.item_tv_2, "${item.num} Videos")
    }

}