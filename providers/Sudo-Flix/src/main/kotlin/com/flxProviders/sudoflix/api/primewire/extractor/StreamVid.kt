package com.flxProviders.sudoflix.api.primewire.extractor

import com.flixclusive.model.provider.link.MediaLink
import com.flixclusive.provider.extractor.EmbedExtractor
import com.flxProviders.sudoflix.api.util.ExtractorHelper.unpackLink
import okhttp3.OkHttpClient

internal class StreamVid(
    client: OkHttpClient
) : EmbedExtractor(client) {
    override val baseUrl = "https://streamvid.net"
    override val name = "StreamVid"

    override suspend fun extract(
        url: String,
        customHeaders: Map<String, String>?,
        onLinkFound: (MediaLink) -> Unit
    ) {
        val stream = unpackLink(
            client = client,
            url = url,
            linkRegex = Regex("""src:"(https://[^"]+)"""")
        )

        onLinkFound(stream)
    }
}