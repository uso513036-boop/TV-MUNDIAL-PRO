package com.example.data

import com.example.model.Channel
import com.example.model.Country
import com.example.model.ProgramItem
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
                country = Country.PERU,
                category = TvCategory.CULTURA,
                // Highly stable verified HLS streams
                streamUrl = "http://bantel-cdn1.iptvperu.tv:1935/btnscrtn/tvperu.stream/playlist.m3u8",
                backupStreamUrls = listOf(
                    "http://190.93.224.42/TV-PERU/index.m3u8",
                    "https://live-evg11.tv360.bitel.com.pe/bitel/telelimaSRT/playlist.m3u8"
                ),
                logoText = "TVP",
                brandColorHex = 0xFFE53935,
                description = "La señal televisiva del Estado peruano con contenidos de cultura, identidad nacional, educación y noticias.",
                broadcastQuality = "FHD 1080p",
                schedule = listOf(
                    ProgramItem("pe_7_1", "Amanecer en el Perú", "Noticias del día, enlace con regiones y pronóstico del tiempo.", TvCategory.NOTICIAS, "06:00", "08:00", 360, 480, "TP", "Jennifer Cerecida"),
                    ProgramItem("pe_7_2", "GeoMundo & Cultura Viva", "Documentales sobre la biodiversidad, historia y maravillas del Perú profundo.", TvCategory.CULTURA, "08:00", "10:30", 480, 630, "TP", "Gonzalo Torres"),
                    ProgramItem("pe_7_3", "Chicos IPe en TV Perú", "Animación infantil y programas interactivos para niños y jóvenes.", TvCategory.INFANTIL, "10:30", "12:00", 630, 720, "TP"),
                    ProgramItem("pe_7_4", "El Noticiero Mediodía", "Cobertura de los sucesos más importantes en Lima y departamentos del Perú.", TvCategory.NOTICIAS, "12:00", "14:00", 720, 840, "+14", "Fátima Saldonid"),
                    ProgramItem("pe_7_5", "Costumbres & Sabores Peruanos", "Viajes por la gastronomía ancestral y tradiciones de la costa, sierra y selva.", TvCategory.CULTURA, "14:00", "16:30", 840, 990, "TP", "Sonaly Tuesta"),
                    ProgramItem("pe_7_6", "Mundo Deportes Perú", "Todo el acontecer del fútbol peruano, Liga 1 y deportistas olímpicos.", TvCategory.DEPORTES, "16:30", "18:00", 990, 1080, "TP", "Coki Gonzáles"),
                    ProgramItem("pe_7_7", "TV Perú Central Edición Noche", "Análisis profundo de la coyuntura nacional con invitados especiales.", TvCategory.NOTICIAS, "18:00", "20:00", 1080, 1200, "+14", "Perla Berríos"),
                    ProgramItem("pe_7_8", "Reportajes Especiales del Bicentenario", "Crónicas y reportajes de investigación sobre la historia peruana contemporánea.", TvCategory.ENTRETENIMIENTO, "20:00", "22:00", 1200, 1320, "+14"),
                    ProgramItem("pe_7_9", "Noche de Gala Criolla y Andina", "Conciertos en vivo y música tradicional peruana en el Gran Teatro Nacional.", TvCategory.MUSICA, "22:00", "24:00", 1320, 1440, "TP")
                )
            ),
            Channel(
                id = "pe_tv_peru_noticias",
                number = 73,
                name = "TV Perú Noticias 7.3",
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
                schedule = listOf(
                    ProgramItem("pe_73_1", "Primera Edición Noticias", "Resumen informativo matutino y estado del tráfico en Lima Metropolitana.", TvCategory.NOTICIAS, "05:00", "08:00", 300, 480, "TP"),
                    ProgramItem("pe_73_2", "Diálogos del Día", "Entrevistas en vivo con analistas económicos y líderes de opinión peruanos.", TvCategory.NOTICIAS, "08:00", "11:00", 480, 660, "+14"),
                    ProgramItem("pe_73_3", "Conexión Regional", "Transmisión en directo con corresponsales en Cusco, Arequipa, Trujillo e Iquitos.", TvCategory.NOTICIAS, "11:00", "13:30", 660, 810, "TP"),
                    ProgramItem("pe_73_4", "Mundo Empresarial & Economía", "Tendencias de los mercados, tipo de cambio y agroexportación peruana.", TvCategory.NOTICIAS, "13:30", "15:30", 810, 930, "TP"),
                    ProgramItem("pe_73_5", "Noticias Tarde Minuto a Minuto", "Actualización continua de los acontecimientos nacionales e internacionales.", TvCategory.NOTICIAS, "15:30", "18:00", 930, 1080, "+14"),
                    ProgramItem("pe_73_6", "Mesa Política Perú", "Debate sobre las principales reformas y decisiones en el Congreso y Ejecutivo.", TvCategory.NOTICIAS, "18:00", "21:00", 1080, 1260, "+14"),
                    ProgramItem("pe_73_7", "Cierre Informativo 24 Horas", "Balance completo de la jornada noticiosa del Perú.", TvCategory.NOTICIAS, "21:00", "24:00", 1260, 1440, "+14")
                )
            ),
            Channel(
                id = "pe_rpp_tv",
                number = 10,
                name = "RPP TV Noticias",
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
                schedule = listOf(
                    ProgramItem("pe_rpp_1", "La Rotativa del Aire", "Información del tránsito, sucesos y entrevistas en vivo.", TvCategory.NOTICIAS, "06:00", "09:00", 360, 540, "TP"),
                    ProgramItem("pe_rpp_2", "Ampliación de Noticias", "La mesa de opinión política más influyente del país.", TvCategory.NOTICIAS, "09:00", "12:00", 540, 720, "+14"),
                    ProgramItem("pe_rpp_3", "RPP Central Mediodía", "Toda la información de Lima y las regiones del interior.", TvCategory.NOTICIAS, "12:00", "15:00", 720, 900, "TP"),
                    ProgramItem("pe_rpp_4", "Fútbol como Cancha", "Debate deportivo con los especialistas del balompié peruano.", TvCategory.DEPORTES, "15:00", "18:00", 900, 1080, "TP"),
                    ProgramItem("pe_rpp_5", "Las Claves del Día", "Análisis con los principales protagonistas de la noticia.", TvCategory.NOTICIAS, "18:00", "21:00", 1080, 1260, "+14"),
                    ProgramItem("pe_rpp_6", "Cierre de Jornada", "Resumen exhaustivo de los acontecimientos del Perú.", TvCategory.NOTICIAS, "21:00", "24:00", 1260, 1440, "TP")
                )
            ),
            Channel(
                id = "pe_usmp_tv",
                number = 33,
                name = "USMP TV Educativo",
                country = Country.PERU,
                category = TvCategory.CULTURA,
                streamUrl = "https://streamusmptv.ddns.net/live/stream.m3u8",
                backupStreamUrls = emptyList(),
                logoText = "USMP",
                brandColorHex = 0xFF2E7D32,
                description = "El primer canal educativo del Perú con clases y lecciones interactivas para primaria y secundaria.",
                broadcastQuality = "HD 720p",
                schedule = listOf(
                    ProgramItem("pe_usmp_1", "Matemática Lúdica para Todos", "Lecciones dinámicas de álgebra y geometría para jóvenes estudiantes.", TvCategory.CULTURA, "07:00", "09:30", 420, 570, "TP"),
                    ProgramItem("pe_usmp_2", "Historia del Perú Ilustrada", "Recorrido por las culturas preincas, el Tawantinsuyo y la gesta libertadora.", TvCategory.CULTURA, "09:30", "12:00", 570, 720, "TP"),
                    ProgramItem("pe_usmp_3", "Ciencia, Química y Biología", "Laboratorio televisivo explicando fenómenos naturales y el cuerpo humano.", TvCategory.CULTURA, "12:00", "14:30", 720, 870, "TP"),
                    ProgramItem("pe_usmp_4", "Comunicación y Literatura Universal", "Grandes obras de la literatura peruana e hispanoamericana.", TvCategory.CULTURA, "14:30", "17:00", 870, 1020, "TP"),
                    ProgramItem("pe_usmp_5", "Inglés Práctico Conversacional", "Clases de idiomas estructuradas para el desarrollo profesional.", TvCategory.CULTURA, "17:00", "19:30", 1020, 1170, "TP"),
                    ProgramItem("pe_usmp_6", "Campus Universitario USMP", "Conferencias magistrales de medicina, derecho e ingeniería.", TvCategory.CULTURA, "19:30", "22:00", 1170, 1320, "TP"),
                    ProgramItem("pe_usmp_7", "Música Clásica y Grandes Sinfonías", "Obras maestras interpretadas por orquestas juveniles del Perú.", TvCategory.MUSICA, "22:00", "24:00", 1320, 1440, "TP")
                )
            ),
            Channel(
                id = "pe_pbo_tv",
                number = 11,
                name = "PBO TV Perú",
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
                schedule = listOf(
                    ProgramItem("pe_pbo_1", "PBO Noticias Matinal", "Noticias del día y entrevistas de coyuntura política.", TvCategory.NOTICIAS, "06:00", "09:00", 360, 540, "TP"),
                    ProgramItem("pe_pbo_2", "Tribuna Abierta", "Panel de discusión y participación de televidentes.", TvCategory.NOTICIAS, "09:00", "12:00", 540, 720, "+14"),
                    ProgramItem("pe_pbo_3", "PBO Central", "Resumen informativo del mediodía.", TvCategory.NOTICIAS, "12:00", "15:00", 720, 900, "TP"),
                    ProgramItem("pe_pbo_4", "Análisis con Phillip Butters", "Opinión y debate sobre los sucesos del país.", TvCategory.NOTICIAS, "18:00", "21:00", 1080, 1260, "+18"),
                    ProgramItem("pe_pbo_5", "Cierre de Noche", "Noticias internacionales y resumen local.", TvCategory.NOTICIAS, "21:00", "24:00", 1260, 1440, "TP")
                )
            ),
            Channel(
                id = "pe_sol_tv",
                number = 21,
                name = "Sol TV Norte",
                country = Country.PERU,
                category = TvCategory.NOTICIAS,
                streamUrl = "https://5790d294af2dc.streamlock.net:443/streamtv/streamtv/playlist.m3u8",
                backupStreamUrls = emptyList(),
                logoText = "SOL",
                brandColorHex = 0xFFFF8F00,
                description = "La señal televisiva del norte peruano con cobertura en Trujillo, Chiclayo, Piura y Chimbote.",
                broadcastQuality = "HD 720p",
                schedule = listOf(
                    ProgramItem("pe_sol_1", "Sol TV Noticias Matinal", "Información de las provincias del norte del país.", TvCategory.NOTICIAS, "06:00", "09:00", 360, 540, "TP"),
                    ProgramItem("pe_sol_2", "Norte en Directo", "Reportajes sobre turismo y cultura costeña.", TvCategory.CULTURA, "09:00", "12:00", 540, 720, "TP"),
                    ProgramItem("pe_sol_3", "Edición Central", "Las noticias más importantes de La Libertad y el Perú.", TvCategory.NOTICIAS, "12:00", "14:30", 720, 870, "+14"),
                    ProgramItem("pe_sol_4", "Deportes en Acción", "Seguimiento de los clubes de la Liga 1 en el norte.", TvCategory.DEPORTES, "14:30", "17:00", 870, 1020, "TP"),
                    ProgramItem("pe_sol_5", "Noche Norteña", "Música de marinera y tradiciones peruanas.", TvCategory.MUSICA, "20:00", "23:00", 1200, 1380, "TP")
                )
            ),
            Channel(
                id = "pe_trivu_tv",
                number = 40,
                name = "Trivu TV Perú",
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
                schedule = listOf(
                    ProgramItem("pe_trv_1", "Despierta Trivu", "Videos musicales, tendencias en redes y entrevistas.", TvCategory.ENTRETENIMIENTO, "07:00", "10:00", 420, 600, "TP"),
                    ProgramItem("pe_trv_2", "Zona Gamer", "Análisis de videojuegos, esports y cultura tech.", TvCategory.ENTRETENIMIENTO, "10:00", "13:00", 600, 780, "TP"),
                    ProgramItem("pe_trv_3", "Cine & Series", "Críticas de estrenos y entrevistas con realizadores.", TvCategory.ENTRETENIMIENTO, "15:00", "18:00", 900, 1080, "+14"),
                    ProgramItem("pe_trv_4", "Trivu Night", "Música alternativa y conciertos en directo.", TvCategory.MUSICA, "20:00", "24:00", 1200, 1440, "TP")
                )
            ),

            // ==========================================
            // CANALES DE COSTA RICA 🇨🇷
            // ==========================================
            Channel(
                id = "cr_canal_8_multimedios",
                number = 8,
                name = "Canal 8 Costa Rica (Multimedios)",
                country = Country.COSTA_RICA,
                category = TvCategory.NOTICIAS,
                streamUrl = "https://mdstrm.com/live-stream-playlist/5a7b1e63a8da282c34d65445.m3u8",
                backupStreamUrls = emptyList(),
                logoText = "C8",
                brandColorHex = 0xFFD81B60,
                description = "Canal 8 de Costa Rica con noticias de última hora, entretenimiento, fútbol de primera división y programas de opinión.",
                broadcastQuality = "HD 1080p",
                schedule = listOf(
                    ProgramItem("cr_8_1", "Telediario Al Minuto Costa Rica", "El primer noticiero matutino con la información del tránsito en el GAM.", TvCategory.NOTICIAS, "05:45", "08:30", 345, 510, "TP"),
                    ProgramItem("cr_8_2", "Divas pero Divinas", "Revista matutina de farándula, moda, psicología y cocina.", TvCategory.ENTRETENIMIENTO, "08:30", "11:30", 510, 690, "TP"),
                    ProgramItem("cr_8_3", "Telediario Mediodía", "Información en directo de los sucesos más destacados en todo el territorio nacional.", TvCategory.NOTICIAS, "11:30", "13:30", 690, 810, "+14"),
                    ProgramItem("cr_8_4", "Fútbol Caliente Costa Rica", "El debate más intenso sobre la Selección de Costa Rica 'La Sele' y clubes ticos.", TvCategory.DEPORTES, "13:30", "16:00", 810, 960, "TP"),
                    ProgramItem("cr_8_5", "Caso Cerrado Costa Rica", "Resolución de conflictos y casos familiares de gran impacto emocional.", TvCategory.ENTRETENIMIENTO, "16:00", "18:00", 960, 1080, "+14"),
                    ProgramItem("cr_8_6", "Telediario Estelar", "Edición central con las investigaciones periodísticas más contundentes.", TvCategory.NOTICIAS, "18:00", "20:30", 1080, 1230, "+14"),
                    ProgramItem("cr_8_7", "La Roncha & Espectáculos", "Los secretos y entrevistas con las estrellas más famosas del espectáculo tico.", TvCategory.ENTRETENIMIENTO, "20:30", "22:30", 1230, 1350, "+14"),
                    ProgramItem("cr_8_8", "Telediario Nocturno", "Resumen de medianoche y análisis de los sucesos de la jornada.", TvCategory.NOTICIAS, "22:30", "24:00", 1350, 1440, "+14")
                )
            ),
            Channel(
                id = "cr_extra_tv_42",
                number = 42,
                name = "Extra TV 42 Costa Rica",
                country = Country.COSTA_RICA,
                category = TvCategory.NOTICIAS,
                streamUrl = "https://d2n1wzrr0aogf5.cloudfront.net/ts:abr.m3u8",
                backupStreamUrls = emptyList(),
                logoText = "X42",
                brandColorHex = 0xFFFF5722,
                description = "El canal del pueblo costarricense. Sucesos en vivo, denuncias ciudadanas, periodismo de impacto y voz comunitaria.",
                broadcastQuality = "HD 720p",
                schedule = listOf(
                    ProgramItem("cr_x42_1", "Extra Noticias Amanecer", "Los sucesos de la madrugada y las denuncias de las comunidades ticas.", TvCategory.NOTICIAS, "06:00", "08:30", 360, 510, "+14"),
                    ProgramItem("cr_x42_2", "Voz del Pueblo en Vivo", "Línea abierta para que los televidentes expresen sus problemáticas locales.", TvCategory.NOTICIAS, "08:30", "11:30", 510, 690, "TP"),
                    ProgramItem("cr_x42_3", "Extra Noticias Mediodía", "La información al rojo vivo desde el lugar de los hechos.", TvCategory.NOTICIAS, "11:30", "13:30", 690, 810, "+18"),
                    ProgramItem("cr_x42_4", "Tribuna Popular", "Abogados y especialistas responden consultas legales y de derechos laborales.", TvCategory.ENTRETENIMIENTO, "13:30", "16:00", 810, 960, "TP"),
                    ProgramItem("cr_x42_5", "Sucesos Extra Tarde", "Cobertura de rescates, bomberos y operativos policiales en San José.", TvCategory.NOTICIAS, "16:00", "18:30", 960, 1110, "+14"),
                    ProgramItem("cr_x42_6", "Extra Noticias Edición Estelar", "El informativo estelar más sintonizado por las comunidades populares.", TvCategory.NOTICIAS, "18:30", "21:00", 1110, 1260, "+18"),
                    ProgramItem("cr_x42_7", "Mesa Redonda Extra", "Debate frontal sobre la política y la realidad socioeconómica de Costa Rica.", TvCategory.NOTICIAS, "21:00", "24:00", 1260, 1440, "+14")
                )
            ),
            Channel(
                id = "cr_vm_latino",
                number = 29,
                name = "VM Latino (Canal Música)",
                country = Country.COSTA_RICA,
                category = TvCategory.MUSICA,
                streamUrl = "https://59ef525c24caa.streamlock.net/vmtv/vmlatino/playlist.m3u8",
                backupStreamUrls = emptyList(),
                logoText = "VM",
                brandColorHex = 0xFF7B1FA2,
                description = "El canal de la música y la juventud de Costa Rica. Conciertos, estrenos mundiales, reggaeton, pop, rock y festivales.",
                broadcastQuality = "HD 1080p",
                schedule = listOf(
                    ProgramItem("cr_vm_1", "Despertar con Éxitos", "Los mejores videos de música urbana y pop latino para empezar el día con energía.", TvCategory.MUSICA, "06:00", "09:00", 360, 540, "TP"),
                    ProgramItem("cr_vm_2", "Los 10 Más Pedidos de Costa Rica", "El conteo oficial con los temas más votados por la audiencia tica.", TvCategory.MUSICA, "09:00", "12:00", 540, 720, "TP"),
                    ProgramItem("cr_vm_3", "Sesiones Acústicas VM", "Artistas costarricenses e internacionales en formato íntimo y acústico.", TvCategory.MUSICA, "12:00", "14:30", 720, 870, "TP"),
                    ProgramItem("cr_vm_4", "Zona Urbana & Reggaeton Hits", "Lo más nuevo de los exponentes del género urbano a nivel global.", TvCategory.MUSICA, "14:30", "17:30", 870, 1050, "+14"),
                    ProgramItem("cr_vm_5", "Clásicos del Rock en Español", "Homenaje a las bandas legendarias que marcaron la historia musical hispana.", TvCategory.MUSICA, "17:30", "20:00", 1050, 1200, "TP"),
                    ProgramItem("cr_vm_6", "Electro Party Costa Rica", "Los mejores DJs de música electrónica en vivo y festivales en Tamarindo.", TvCategory.MUSICA, "20:00", "22:30", 1200, 1350, "+14"),
                    ProgramItem("cr_vm_7", "Trasnochando con VM", "Videoclips continuos sin interrupción para acompañar tu noche.", TvCategory.MUSICA, "22:30", "24:00", 1350, 1440, "TP")
                )
            ),
            Channel(
                id = "cr_canal_1",
                number = 1,
                name = "Canal 1 Costa Rica",
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
                schedule = listOf(
                    ProgramItem("cr_c1_1", "Amanecer Costarricense", "Magazine matutino con recetas, salud y entrevistas.", TvCategory.ENTRETENIMIENTO, "06:30", "09:00", 390, 540, "TP"),
                    ProgramItem("cr_c1_2", "Noticiero Central 1", "Información de las 7 provincias costarricenses.", TvCategory.NOTICIAS, "12:00", "14:00", 720, 840, "+14"),
                    ProgramItem("cr_c1_3", "Cultura & Paisajes Ticos", "Biodiversidad, volcanes y playas costarricenses.", TvCategory.CULTURA, "15:00", "17:30", 900, 1050, "TP"),
                    ProgramItem("cr_c1_4", "Noche de Estrellas", "Series y películas de gran impacto.", TvCategory.ENTRETENIMIENTO, "20:00", "23:00", 1200, 1380, "TP")
                )
            ),
            Channel(
                id = "cr_tv_sur_14",
                number = 14,
                name = "TV Sur Canal 14",
                country = Country.COSTA_RICA,
                category = TvCategory.CULTURA,
                streamUrl = "http://tv.ticosmedia.com:1935/TVSUR/TVSUR/playlist.m3u8",
                backupStreamUrls = listOf(
                    "https://k20.usastreams.com:8081/tvsur/tracks-v1a1/mono.ts.m3u8",
                    "http://tvn.obix.tv:1935/TVN/CH14.stream_720p/playlist.m3u8"
                ),
                logoText = "S14",
                brandColorHex = 0xFF0097A7,
                description = "Voz y sentir del cantón de Pérez Zeledón y la Región Brunca de Costa Rica.",
                broadcastQuality = "HD 720p",
                schedule = listOf(
                    ProgramItem("cr_sur_1", "Amanecer Brunca", "Noticias rurales, agricultura y clima de Pérez Zeledón.", TvCategory.NOTICIAS, "06:00", "08:30", 360, 510, "TP"),
                    ProgramItem("cr_sur_2", "Tradición Sureña", "Historias de los pioneros y vida comunitaria.", TvCategory.CULTURA, "08:30", "11:30", 510, 690, "TP"),
                    ProgramItem("cr_sur_3", "Noticiero Regional Sur", "Actualidad del Pacífico Sur y San José.", TvCategory.NOTICIAS, "11:30", "13:30", 690, 810, "TP"),
                    ProgramItem("cr_sur_4", "Música Campesina", "Folclor y notas autóctonas costarricenses.", TvCategory.MUSICA, "18:00", "21:00", 1080, 1260, "TP")
                )
            ),
            Channel(
                id = "cr_agrotendencia",
                number = 50,
                name = "Agrotendencia TV",
                country = Country.COSTA_RICA,
                category = TvCategory.CULTURA,
                streamUrl = "https://5fc584f3f19c9.streamlock.net/agrotendencia/videoagrotendencia_hls1/playlist.m3u8",
                backupStreamUrls = emptyList(),
                logoText = "AGRO",
                brandColorHex = 0xFF388E3C,
                description = "Canal agropecuario con cobertura de tecnología agrícola, ganadería y ecología de Centroamérica.",
                broadcastQuality = "HD 720p",
                schedule = listOf(
                    ProgramItem("cr_agro_1", "El Campo Hoy", "Informes técnicos de agronomía y cosechas sostenibles.", TvCategory.CULTURA, "06:00", "09:00", 360, 540, "TP"),
                    ProgramItem("cr_agro_2", "Ganadería Moderna", "Técnicas de producción ganadera y sanidad animal.", TvCategory.CULTURA, "09:00", "12:00", 540, 720, "TP"),
                    ProgramItem("cr_agro_3", "Mercados del Café y Cacao", "Cotizaciones internacionales y valor agregado.", TvCategory.NOTICIAS, "14:00", "16:30", 840, 990, "TP"),
                    ProgramItem("cr_agro_4", "Ecosistemas y Agua", "Conservación de suelos y cuencas hidrográficas.", TvCategory.CULTURA, "19:00", "22:00", 1140, 1320, "TP")
                )
            )
        )
    }
}
