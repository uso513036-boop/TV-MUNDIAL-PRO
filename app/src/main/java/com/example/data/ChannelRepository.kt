package com.example.data

import com.example.model.Channel
import com.example.model.Country
import com.example.model.TvCategory

object ChannelRepository {

    fun getChannels(): List<Channel> {
        return listOf(
            // ==========================================
            // CANALES DE PERÚ 🇵🇪
            // ==========================================
            Channel(
                id = "pe_tv_peru",
                number = 7,
                name = "TV Perú (Canal 7)",
                shortName = "TV Perú",
                country = Country.PERU,
                category = TvCategory.CULTURA,
                streamUrl = "http://bantel-cdn1.iptvperu.tv:1935/btnscrtn/tvperu.stream/playlist.m3u8",
                backupStreamUrls = listOf(
                    "http://190.93.224.42/TV-PERU/index.m3u8",
                    "https://live-evg11.tv360.bitel.com.pe/bitel/telelimaSRT/playlist.m3u8"
                ),
                logoText = "TVP",
                brandColorHex = 0xFFE53935,
                description = "La señal televisiva del Estado peruano con contenidos de cultura, identidad nacional, educación y noticias.",
                broadcastQuality = "FHD 1080p",
                epgAliases = listOf("TV.PERU.HD", "TV.PERU", "TVPeru.pe", "TV Perú", "Canal 7", "TVPERU", "Canal.7.pe", "TV Peru")
            ),
            Channel(
                id = "pe_tv_peru_noticias",
                number = 73,
                name = "TV Perú Noticias 7.3",
                shortName = "TV Perú Noticias",
                country = Country.PERU,
                category = TvCategory.NOTICIAS,
                streamUrl = "http://bantel-cdn1.iptvperu.tv:1935/btnscrtn/tvperunoticias.stream/playlist.m3u8",
                backupStreamUrls = listOf(
                    "http://190.93.224.42/TV-PERU-NOTICIAS/index.m3u8",
                    "http://bantel-cdn1.iptvperu.tv:1935/btnscrtn/tvperu.stream/playlist.m3u8"
                ),
                logoText = "7.3",
                brandColorHex = 0xFFB71C1C,
                description = "Señal informativa continua 24 horas del Perú con despachos en vivo desde todas las provincias.",
                broadcastQuality = "HD 720p",
                epgAliases = listOf("TV.PERU.7.3", "TVPeruNoticias.pe", "TV Perú Noticias", "7.3", "TVPerú Noticias", "TV Peru Noticias")
            ),
            Channel(
                id = "pe_rpp_tv",
                number = 10,
                name = "RPP TV Noticias",
                shortName = "RPP Noticias",
                country = Country.PERU,
                category = TvCategory.NOTICIAS,
                streamUrl = "https://redirector.rudo.video/hls-video/567ffde3fa319fadf3419efda25619456231dfea/rpptv/rpptv.smil/playlist.m3u8",
                backupStreamUrls = listOf(
                    "http://190.93.224.42/RPP/index.m3u8",
                    "http://bantel-cdn1.iptvperu.tv:1935/btnscrtn/RPP/playlist.m3u8"
                ),
                logoText = "RPP",
                brandColorHex = 0xFFFFD600,
                description = "El canal de noticias líder en radio y televisión del Perú con información minuto a minuto.",
                broadcastQuality = "HD 1080p",
                epgAliases = listOf("RPP.HD.(RPP.HD).pe", "RPP.TV.(RPP.TV).pe", "RPP.pe", "RPP", "RPP TV", "RPP Noticias")
            ),
            Channel(
                id = "pe_usmp_tv",
                number = 33,
                name = "USMP TV Educativo",
                shortName = "USMP TV",
                country = Country.PERU,
                category = TvCategory.CULTURA,
                streamUrl = "http://190.93.224.42/USMP/index.m3u8",
                backupStreamUrls = listOf(
                    "http://187.102.210.46/USMP/index.m3u8",
                    "https://streamusmptv.ddns.net/live/stream.m3u8",
                    "https://live-evg11.tv360.bitel.com.pe/bitel/usmp/playlist.m3u8"
                ),
                logoText = "USMP",
                brandColorHex = 0xFF2E7D32,
                description = "El primer canal educativo del Perú con clases y lecciones interactivas para primaria y secundaria.",
                broadcastQuality = "FHD 1080p",
                epgAliases = listOf("USMP.TV.(USMP.TV).pe", "USMP.pe", "USMP TV", "USMP", "USMPTV")
            ),
            Channel(
                id = "pe_pbo_tv",
                number = 11,
                name = "PBO TV Perú",
                shortName = "PBO TV",
                country = Country.PERU,
                category = TvCategory.NOTICIAS,
                streamUrl = "https://live-evg11.tv360.bitel.com.pe/bitel/pbo_abr/playlist.m3u8",
                backupStreamUrls = listOf(
                    "http://190.93.224.42/PBO/index.m3u8"
                ),
                logoText = "PBO",
                brandColorHex = 0xFF00838F,
                description = "Canal de noticias, política y análisis independiente con cobertura nacional.",
                broadcastQuality = "FHD 1080p",
                epgAliases = listOf("PBOTV.pe", "PBO.pe", "PBO TV", "PBO", "PBORadio.pe")
            ),
            Channel(
                id = "pe_sol_tv",
                number = 21,
                name = "Sol TV Norte",
                shortName = "Sol TV",
                country = Country.PERU,
                category = TvCategory.NOTICIAS,
                streamUrl = "https://5790d294af2dc.streamlock.net:443/streamtv/streamtv/playlist.m3u8",
                backupStreamUrls = emptyList(),
                logoText = "SOL",
                brandColorHex = 0xFFFF8F00,
                description = "La señal televisiva del norte peruano con cobertura en Trujillo, Chiclayo, Piura y Chimbote.",
                broadcastQuality = "HD 720p",
                epgAliases = listOf("SolTV.pe", "Sol TV", "Sol TV Norte", "SolTV")
            ),
            Channel(
                id = "pe_trivu_tv",
                number = 40,
                name = "Trivu TV Perú",
                shortName = "Trivu TV",
                country = Country.PERU,
                category = TvCategory.ENTRETENIMIENTO,
                streamUrl = "https://stream2.trivutv.com/memfs/1bc8358a-665e-4bc1-a580-e4fcb54b5103.m3u8",
                backupStreamUrls = listOf(
                    "http://190.93.224.42/TRIVU-TV/index.m3u8"
                ),
                logoText = "TRV",
                brandColorHex = 0xFF7C4DFF,
                description = "Canal de entretenimiento, cultura pop, música y cine para el público joven.",
                broadcastQuality = "HD 1080p",
                epgAliases = listOf("CANAL.J.(Canal.J).pe", "TrivuTV.pe", "Trivu TV", "Trivu")
            ),

            // ==========================================
            // CANALES DE COSTA RICA 🇨🇷
            // ==========================================
            Channel(
                id = "cr_canal_8_multimedios",
                number = 8,
                name = "Multimedios Canal 8",
                shortName = "Multimedios",
                country = Country.COSTA_RICA,
                category = TvCategory.NOTICIAS,
                streamUrl = "https://mdstrm.com/live-stream-playlist/5a7b1e63a8da282c34d65445.m3u8",
                backupStreamUrls = emptyList(),
                logoText = "MM",
                brandColorHex = 0xFFD81B60,
                description = "Señal de Multimedios Costa Rica con noticias de última hora, entretenimiento, fútbol de primera división y programas de opinión.",
                broadcastQuality = "HD 1080p",
                epgAliases = listOf("Canal.Multimedios.(Costa.Rica).cr", "Canal8.cr", "Multimedios", "Canal 8", "Canal 8 Costa Rica")
            ),
            Channel(
                id = "cr_extra_tv_42",
                number = 42,
                name = "Extra TV 42 Costa Rica",
                shortName = "Extra TV 42",
                country = Country.COSTA_RICA,
                category = TvCategory.NOTICIAS,
                streamUrl = "https://d2n1wzrr0aogf5.cloudfront.net/ts:abr.m3u8",
                backupStreamUrls = emptyList(),
                logoText = "X42",
                brandColorHex = 0xFFFF5722,
                description = "El canal del pueblo costarricense. Sucesos en vivo, denuncias ciudadanas, periodismo de impacto y voz comunitaria.",
                broadcastQuality = "HD 720p",
                epgAliases = listOf("Canal.Extra.TV.42.de.Costa.Rica.cr", "ExtraTV42.cr", "Extra TV 42", "Extra TV", "Canal 42")
            ),
            Channel(
                id = "cr_vm_latino",
                number = 29,
                name = "VM Latino (Canal Música)",
                shortName = "VM Latino",
                country = Country.COSTA_RICA,
                category = TvCategory.MUSICA,
                streamUrl = "https://59ef525c24caa.streamlock.net/vmtv/vmlatino/playlist.m3u8",
                backupStreamUrls = emptyList(),
                logoText = "VM",
                brandColorHex = 0xFF7B1FA2,
                description = "El canal de la música y la juventud de Costa Rica. Conciertos, estrenos mundiales, reggaeton, pop, rock y festivales.",
                broadcastQuality = "HD 1080p",
                epgAliases = listOf("VMLatino.cr", "VM Latino", "Canal 29", "VM Latino Costa Rica")
            ),
            Channel(
                id = "cr_canal_1",
                number = 1,
                name = "Canal 1 Costa Rica",
                shortName = "Canal 1",
                country = Country.COSTA_RICA,
                category = TvCategory.ENTRETENIMIENTO,
                streamUrl = "https://vid.canal1cr.com:3424/multi_live/play.m3u8",
                backupStreamUrls = listOf(
                    "https://vid.canal1cr.com:3424/multi_live/play_720.m3u8"
                ),
                logoText = "C1",
                brandColorHex = 0xFF0D47A1,
                description = "Señal abierta costarricense con programación de entretenimiento, cultura, noticias y series.",
                broadcastQuality = "HD 720p",
                epgAliases = listOf("Canal.1.de.Costa.Rica.cr", "Canal1.cr", "Canal 1", "Canal 1 Costa Rica")
            ),
            Channel(
                id = "cr_tv_sur_14",
                number = 14,
                name = "TV Sur Canal 14",
                shortName = "TV Sur",
                country = Country.COSTA_RICA,
                category = TvCategory.CULTURA,
                streamUrl = "https://k20.usastreams.com:8081/tvsur/index.m3u8",
                backupStreamUrls = listOf(
                    "https://k20.usastreams.com:8081/tvsur/tracks-v1a1/mono.ts.m3u8"
                ),
                logoText = "S14",
                brandColorHex = 0xFF0097A7,
                description = "Voz y sentir del cantón de Pérez Zeledón y la Región Brunca de Costa Rica.",
                broadcastQuality = "HD 1080p",
                epgAliases = listOf("TVSurCanal14.cr", "TVSur.cr", "TV Sur", "Canal 14")
            ),
            Channel(
                id = "cr_agrotendencia",
                number = 50,
                name = "Agrotendencia TV",
                shortName = "Agrotendencia",
                country = Country.COSTA_RICA,
                category = TvCategory.CULTURA,
                streamUrl = "https://5fc584f3f19c9.streamlock.net/agrotendencia/videoagrotendencia_hls1/playlist.m3u8",
                backupStreamUrls = emptyList(),
                logoText = "AGRO",
                brandColorHex = 0xFF388E3C,
                description = "Canal agropecuario con cobertura de tecnología agrícola, ganadería y ecología de Centroamérica.",
                broadcastQuality = "HD 720p",
                epgAliases = listOf(
                    "Canal.Agrotendencia.TV.cr",
                    "AgrotendenciaTV.cr@SD",
                    "AgrotendenciaTV.cr",
                    "agrotendencia_tv",
                    "Canal Agrotendencia TV",
                    "Agrotendencia TV",
                    "Agrotendencia",
                    "agrotendencia.tv",
                    "Agrotendencia.cr",
                    "AgrotendenciaTV.ve",
                    "agrotendenciatv"
                )
            )
        )
    }
}
