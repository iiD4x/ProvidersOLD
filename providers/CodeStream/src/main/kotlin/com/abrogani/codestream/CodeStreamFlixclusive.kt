package com.abrogani.codestream

import android.content.Context
import com.flixclusive.provider.FlixclusiveProvider
import com.flixclusive.provider.Provider
import com.abrogani.codestream.cs.*
import com.lagradost.cloudstream3.extractors.DoodYtExtractor
import com.lagradost.cloudstream3.extractors.FileMoon
import com.lagradost.cloudstream3.extractors.Gofile
import com.lagradost.cloudstream3.extractors.MixDrop
import com.lagradost.cloudstream3.extractors.Moviesapi
import com.lagradost.cloudstream3.extractors.Mp4Upload
import com.lagradost.cloudstream3.extractors.StreamTape
import com.lagradost.cloudstream3.extractors.StreamWishExtractor
import com.lagradost.cloudstream3.extractors.VidHidePro6
import com.lagradost.cloudstream3.extractors.VidSrcExtractor
import com.lagradost.cloudstream3.extractors.Vidmolyme
import com.lagradost.cloudstream3.extractors.Vidplay
import com.lagradost.cloudstream3.extractors.Voe
import com.lagradost.cloudstream3.utils.ExtractorApi
import com.lagradost.cloudstream3.utils.extractorApis
import okhttp3.OkHttpClient

internal const val CODE_STREAM_NAME = "CodeStream"

@FlixclusiveProvider
class CodeStreamFlixclusive : Provider() {
    override val name: String get() = CODE_STREAM_NAME
    override fun getApi(
        context: Context,
        client: OkHttpClient
    ) = CodeStreamAPI(
        client = client,
        provider = this
    )

    fun registerExtractor(extractor: ExtractorApi) {
        extractorApis.add(extractor)

    }

    init {
        registerExtractor(Animefever())
        registerExtractor(Multimovies())
        registerExtractor(MultimoviesSB())
        registerExtractor(Yipsu())
        registerExtractor(Mwish())
        registerExtractor(TravelR())
        registerExtractor(Playm4u())
        registerExtractor(Vidplay())
        registerExtractor(FileMoon())
        registerExtractor(VCloud())
        registerExtractor(Bestx())
        registerExtractor(Filelions())
        registerExtractor(Snolaxstream())
        registerExtractor(Pixeldra())
        registerExtractor(Mp4Upload())
        registerExtractor(Graceaddresscommunity())
        registerExtractor(M4ufree())
        registerExtractor(Streamruby())
        registerExtractor(StreamWishExtractor())
        registerExtractor(Filelion())
        registerExtractor(DoodYtExtractor())
        registerExtractor(dlions())
        registerExtractor(MixDrop())
        registerExtractor(dwish())
        registerExtractor(Embedwish())
        registerExtractor(UqloadsXyz())
        registerExtractor(Uploadever())
        registerExtractor(Netembed())
        registerExtractor(Flaswish())
        registerExtractor(Comedyshow())
        registerExtractor(Ridoo())
        registerExtractor(Streamvid())
        registerExtractor(StreamTape())
        registerExtractor(do0od())
        registerExtractor(Embedrise())
        registerExtractor(Gdmirrorbot())
        registerExtractor(FilemoonNl())
        registerExtractor(Alions())
        registerExtractor(Vidmolyme())
        registerExtractor(AllinoneDownloader())
        registerExtractor(Tellygossips())
        registerExtractor(Tvlogy())
        registerExtractor(Voe())
        registerExtractor(Mdrive())
        registerExtractor(Gofile())
        registerExtractor(Animezia())
        registerExtractor(Moviesapi())
        registerExtractor(PixelDrain())
        registerExtractor(Modflix())
        registerExtractor(Vectorx())
        registerExtractor(Sethniceletter())
        registerExtractor(GDFlix())
        registerExtractor(fastdlserver())
        registerExtractor(Asianbxkiun())
        registerExtractor(GDFlix1())
        registerExtractor(GDFlix2())
        registerExtractor(furher())
        registerExtractor(VidSrcExtractor())
        registerExtractor(Servertwo())
        registerExtractor(MultimoviesAIO())
        registerExtractor(HubCloud())
        registerExtractor(HubCloudClub())
        registerExtractor(HubCloudlol())
        registerExtractor(Driveseed())
        registerExtractor(Driveleech())
        registerExtractor(VidHidePro6())
        registerExtractor(MixDropSi())
        registerExtractor(Mp4Upload())
    }
}