package vanphuc0497.job.mycv.data.model.response.base

import org.simpleframework.xml.*

/**
 * Create by Nguyen Van Phuc on 3/16/20
 */
@Root(name = "rss", strict = false)
open class BaseRssResponse(
    @field:Path("channel")
    @field:Element(name = "title", required = false)
    @param:Element(name = "title", required = false)
    val channelTitle: String? = null,

    @field:Path("channel")
    @field:Element(name = "description", required = false)
    @param:Element(name = "description", required = false)
    val description: String? = null,

    @field:Path("channel")
    @field:ElementList(entry = "item", inline = true)
    @param:ElementList(entry = "item", inline = true)
    val items: List<BaseRssItem>? = null
)

@Root(strict = false)
data class BaseRssItem(
    @field:Element(name = "title", required = false)
    @param:Element(name = "title", required = false)
    val title: String? = null,

    @field:Path("description")
    @field:Element(name = "img", required = false)
    @param:Element(name = "img", required = false)
    val imageInfo: BaseRssImageThumbnail? = null,

    @field:Element(name = "link", required = false)
    @param:Element(name = "link", required = false)
    val link: String? = null,

    @field:Element(name = "guid", required = false)
    @param:Element(name = "guid", required = false)
    val guid: String? = null,

    @field:Element(name = "pubDate", required = false)
    @param:Element(name = "pubDate", required = false)
    val pubDate: String? = null
)

@Root(name = "img", strict = true)
data class BaseRssImageThumbnail(
    @field:Attribute(name = "src", required = false)
    @param:Attribute(name = "src", required = false)
    val imageUrl: String? = null,

    @field:Attribute(name = "width", required = false)
    @param:Attribute(name = "width", required = false)
    val imageWidth: String? = null,

    @field:Attribute(name = "height", required = false)
    @param:Attribute(name = "height", required = false)
    val imageHeight: String? = null
)
