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
                // TV Perú official / public live stream
                streamUrl = "https://cdnhd.iblups.com/hls/tvperuhd.m3u8",
                backupStreamUrls = listOf(
                    "https://live-edge01.telecentro.net.ar/live/smil:tvp.smil/playlist.m3u8",
                    "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
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
                streamUrl = "https://tvperunoticias.akamaized.net/hls/live/2034907/tvperunoticias/master.m3u8",
                backupStreamUrls = listOf(
                    "https://cdnhd.iblups.com/hls/tvperuhd.m3u8",
                    "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4"
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
                id = "pe_canal_ipe",
                number = 74,
                name = "Canal IPe (Perú)",
                country = Country.PERU,
                category = TvCategory.INFANTIL,
                streamUrl = "https://canalipe.akamaized.net/hls/live/2034908/canalipe/master.m3u8",
                backupStreamUrls = listOf(
                    "https://cdnhd.iblups.com/hls/tvperuhd.m3u8",
                    "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4"
                ),
                logoText = "IPe",
                brandColorHex = 0xFFFF6D00,
                description = "El canal cultural y entretenido de la infancia y juventud del Perú. Creatividad, ciencia y diversión.",
                broadcastQuality = "HD 1080p",
                schedule = listOf(
                    ProgramItem("pe_ipe_1", "Chicos IPe Despierta", "Aventuras animadas y canciones para los más pequeños.", TvCategory.INFANTIL, "06:00", "09:00", 360, 540, "TP"),
                    ProgramItem("pe_ipe_2", "Ciencia en Acción Perú", "Experimentos sencillos, robótica escolar y descubrimientos científicos.", TvCategory.CULTURA, "09:00", "11:30", 540, 690, "TP"),
                    ProgramItem("pe_ipe_3", "Mundo de Monstruos Andinos", "Serie de animación que rescata los mitos y leyendas de la cosmovisión andina.", TvCategory.INFANTIL, "11:30", "13:30", 690, 810, "TP"),
                    ProgramItem("pe_ipe_4", "Hazlo en Casa: Arte y Diseño", "Talleres creativos de dibujo, reciclaje y modelado.", TvCategory.INFANTIL, "13:30", "16:00", 810, 960, "TP"),
                    ProgramItem("pe_ipe_5", "Zona Gamer & Tech", "Novedades de videojuegos peruanos, tecnología y cultura pop.", TvCategory.ENTRETENIMIENTO, "16:00", "18:30", 960, 1110, "TP"),
                    ProgramItem("pe_ipe_6", "Documentales Jóvenes del Perú", "Historias de innovación social y superación en comunidades de todo el país.", TvCategory.CULTURA, "18:30", "21:00", 1110, 1260, "TP"),
                    ProgramItem("pe_ipe_7", "Cine Corto IPe", "Selección de cortometrajes independientes de realizadores universitarios peruanos.", TvCategory.CULTURA, "21:00", "24:00", 1260, 1440, "+14")
                )
            ),
            Channel(
                id = "pe_congreso_tv",
                number = 56,
                name = "Congreso TV Perú",
                country = Country.PERU,
                category = TvCategory.NOTICIAS,
                streamUrl = "https://live-congreso.smartstream.pe/live/congresotv.m3u8",
                backupStreamUrls = listOf(
                    "https://cdnhd.iblups.com/hls/tvperuhd.m3u8",
                    "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4"
                ),
                logoText = "CTV",
                brandColorHex = 0xFF1565C0,
                description = "Transmisión en directo de las sesiones del Pleno del Congreso de la República del Perú y comisiones legislativas.",
                broadcastQuality = "HD 1080p",
                schedule = listOf(
                    ProgramItem("pe_ctv_1", "Agenda Parlamentaria Matutina", "Programación de las sesiones de comisiones del día.", TvCategory.NOTICIAS, "07:00", "09:00", 420, 540, "TP"),
                    ProgramItem("pe_ctv_2", "Sesión de Comisiones en Vivo", "Debates en la Comisión de Presupuesto y Constitución.", TvCategory.NOTICIAS, "09:00", "13:00", 540, 780, "TP"),
                    ProgramItem("pe_ctv_3", "Resumen Informativo Legislativo", "Proyectos de ley aprobados y declaraciones de los voceros.", TvCategory.NOTICIAS, "13:00", "14:30", 780, 870, "TP"),
                    ProgramItem("pe_ctv_4", "Sesión del Pleno del Congreso EN VIVO", "Debate y votación de leyes de trascendencia nacional.", TvCategory.NOTICIAS, "14:30", "19:00", 870, 1140, "TP"),
                    ProgramItem("pe_ctv_5", "Tribuna Democrática", "Análisis con especialistas sobre el impacto de las normas aprobadas.", TvCategory.NOTICIAS, "19:00", "22:00", 1140, 1320, "+14"),
                    ProgramItem("pe_ctv_6", "Archivo Histórico del Parlamento", "Documentales sobre presidentes y momentos cumbres de la historia republicana.", TvCategory.CULTURA, "22:00", "24:00", 1320, 1440, "TP")
                )
            ),
            Channel(
                id = "pe_usmp_tv",
                number = 33,
                name = "USMP TV Educativo",
                country = Country.PERU,
                category = TvCategory.CULTURA,
                streamUrl = "https://live.usmptv.pe/hls/usmptv.m3u8",
                backupStreamUrls = listOf(
                    "https://canalipe.akamaized.net/hls/live/2034908/canalipe/master.m3u8",
                    "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/WeAreGoingOnBullrun.mp4"
                ),
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
                id = "pe_bethel_tv",
                number = 25,
                name = "Bethel Televisión Perú",
                country = Country.PERU,
                category = TvCategory.ENTRETENIMIENTO,
                streamUrl = "https://betheltv.streamguys1.com/live/betheltv/playlist.m3u8",
                backupStreamUrls = listOf(
                    "https://cdnhd.iblups.com/hls/tvperuhd.m3u8",
                    "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/WhatCarCanYouGetForAGrand.mp4"
                ),
                logoText = "BTV",
                brandColorHex = 0xFF00838F,
                description = "Canal familiar peruano con transmisión mundial, programas de valores, salud, música y orientación comunitaria.",
                broadcastQuality = "FHD 1080p",
                schedule = listOf(
                    ProgramItem("pe_btv_1", "Despertar Familiar", "Reflexiones matutinas, consejos de convivencia y salud preventiva.", TvCategory.ENTRETENIMIENTO, "06:00", "08:30", 360, 510, "TP"),
                    ProgramItem("pe_btv_2", "Mundo Infantil Bethel", "Historias con valores, títeres y canciones para el hogar.", TvCategory.INFANTIL, "08:30", "11:00", 510, 660, "TP"),
                    ProgramItem("pe_btv_3", "Vida Sana y Nutrición Peruana", "Consejos de especialistas sobre alimentación natural y bienestar.", TvCategory.ENTRETENIMIENTO, "11:00", "13:30", 660, 810, "TP"),
                    ProgramItem("pe_btv_4", "Encuentro con las Naciones", "Reportajes sobre proyectos humanitarios en los cinco continentes.", TvCategory.CULTURA, "13:30", "16:00", 810, 960, "TP"),
                    ProgramItem("pe_btv_5", "Música de Paz e Inspiración", "Conciertos corales y melodías acústicas para toda la familia.", TvCategory.MUSICA, "16:00", "18:30", 960, 1110, "TP"),
                    ProgramItem("pe_btv_6", "Valores para la Sociedad", "Conferencias familiares y debates sobre ética comunitaria.", TvCategory.ENTRETENIMIENTO, "18:30", "21:30", 1110, 1290, "TP"),
                    ProgramItem("pe_btv_7", "Noche de Esperanza", "Espacio de reflexión nocturna y orientación familiar.", TvCategory.ENTRETENIMIENTO, "21:30", "24:00", 1290, 1440, "TP")
                )
            ),
            Channel(
                id = "pe_nativa_tv",
                number = 36,
                name = "Nativa TV Perú",
                country = Country.PERU,
                category = TvCategory.DEPORTES,
                streamUrl = "https://edge01.iptv.stream.nativa.pe/live/nativatv.m3u8",
                backupStreamUrls = listOf(
                    "https://live-edge01.telecentro.net.ar/live/smil:tvp.smil/playlist.m3u8",
                    "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/SubaruOutbackSeeTheWorld.mp4"
                ),
                logoText = "NTV",
                brandColorHex = 0xFF303F9F,
                description = "La señal multiplataforma del deporte peruano, fútbol femenino, Liga 2, vóley y noticias sin filtro.",
                broadcastQuality = "HD 1080p",
                schedule = listOf(
                    ProgramItem("pe_ntv_1", "Nativa Deportes al Día", "La mejor previa de los encuentros del fútbol peruano y Copa Libertadores.", TvCategory.DEPORTES, "07:00", "09:30", 420, 570, "TP"),
                    ProgramItem("pe_ntv_2", "Noticias con Opinión", "Análisis periodístico de los eventos que marcan la agenda del país.", TvCategory.NOTICIAS, "09:30", "12:00", 570, 720, "+14"),
                    ProgramItem("pe_ntv_3", "Vóley Peruano: Liga Superior", "Resúmenes, entrevistas exclusivas con las voleibolistas nacionales.", TvCategory.DEPORTES, "12:00", "14:30", 720, 870, "TP"),
                    ProgramItem("pe_ntv_4", "Tribuna Femenina", "El desarrollo del fútbol femenino y atletas de alta competencia en el Perú.", TvCategory.DEPORTES, "14:30", "17:00", 870, 1020, "TP"),
                    ProgramItem("pe_ntv_5", "Fútbol en Vivo: La Previa", "Alineaciones, estadísticas y clima previo a los partidos de la fecha.", TvCategory.DEPORTES, "17:00", "19:30", 1020, 1170, "TP"),
                    ProgramItem("pe_ntv_6", "Nativa Noche: Debate Deportivo", "La mesa de discusión más apasionada con exjugadores y cronistas.", TvCategory.DEPORTES, "19:30", "22:00", 1170, 1320, "+14"),
                    ProgramItem("pe_ntv_7", "Tercer Tiempo", "Post-partido, análisis de jugadas polémicas y tabla de posiciones.", TvCategory.DEPORTES, "22:00", "24:00", 1320, 1440, "+14")
                )
            ),

            // ==========================================
            // CANALES DE COSTA RICA 🇨🇷
            // ==========================================
            Channel(
                id = "cr_canal_13_sinart",
                number = 13,
                name = "Canal 13 Costa Rica (SINART)",
                country = Country.COSTA_RICA,
                category = TvCategory.CULTURA,
                // Costa Rica public broadcaster
                streamUrl = "https://5a7c5c2d33451.streamlock.net/costarica13/live/playlist.m3u8",
                backupStreamUrls = listOf(
                    "https://edge.sinartgo.com/live/canal13/index.m3u8",
                    "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
                ),
                logoText = "C13",
                brandColorHex = 0xFF0D47A1,
                description = "Televisión de Costa Rica (SINART). Información veraz, fomento de la cultura, tradiciones campesinas y arte costarricense.",
                broadcastQuality = "FHD 1080p",
                schedule = listOf(
                    ProgramItem("cr_13_1", "Café Nacional de Costa Rica", "El magazine matutino tradicional con recetas ticas, salud y emprendimientos.", TvCategory.ENTRETENIMIENTO, "06:30", "09:00", 390, 540, "TP", "Sergio Castro"),
                    ProgramItem("cr_13_2", "Costa Rica Verde & Pura Vida", "Reportajes sobre los parques nacionales, biodiversidad y volcanes del país.", TvCategory.CULTURA, "09:00", "11:00", 540, 660, "TP"),
                    ProgramItem("cr_13_3", "Treche Noticias Edición Mediodía", "Toda la actualidad de las 7 provincias costarricenses.", TvCategory.NOTICIAS, "11:00", "13:00", 660, 780, "+14"),
                    ProgramItem("cr_13_4", "Identidades Ticas", "Músicos, pintores y saberes ancestrales de Guanacaste, Cartago y Limón.", TvCategory.CULTURA, "13:00", "15:30", 780, 930, "TP"),
                    ProgramItem("cr_13_5", "Zona Deportiva Costarricense", "Fútbol nacional de la UNAFUT, ciclismo y surf en las playas ticas.", TvCategory.DEPORTES, "15:30", "18:00", 930, 1080, "TP"),
                    ProgramItem("cr_13_6", "Treche Noticias Estelar", "El análisis profundo de los temas legislativos y económicos de San José.", TvCategory.NOTICIAS, "18:00", "20:30", 1080, 1230, "+14"),
                    ProgramItem("cr_13_7", "Cultura & Cine SINART", "Cine iberoamericano y documentales galardonados.", TvCategory.ENTRETENIMIENTO, "20:30", "22:30", 1230, 1350, "+14"),
                    ProgramItem("cr_13_8", "Noche de Baladas y Calypso", "La riqueza rítmica caribeña de Puerto Viejo y sones costarricenses.", TvCategory.MUSICA, "22:30", "24:00", 1350, 1440, "TP")
                )
            ),
            Channel(
                id = "cr_canal_8_multimedios",
                number = 8,
                name = "Canal 8 Costa Rica (Multimedios)",
                country = Country.COSTA_RICA,
                category = TvCategory.NOTICIAS,
                streamUrl = "https://live.multimedios.cr/hls/canal8cr.m3u8",
                backupStreamUrls = listOf(
                    "https://5a7c5c2d33451.streamlock.net/costarica13/live/playlist.m3u8",
                    "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4"
                ),
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
                id = "cr_vm_latino",
                number = 29,
                name = "VM Latino (El Canal de la Música)",
                country = Country.COSTA_RICA,
                category = TvCategory.MUSICA,
                streamUrl = "https://stream.vmlatino.com/live/vmlatino.m3u8",
                backupStreamUrls = listOf(
                    "https://edge.sinartgo.com/live/canal13/index.m3u8",
                    "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4"
                ),
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
                id = "cr_extra_tv_42",
                number = 42,
                name = "Extra TV 42 Costa Rica",
                country = Country.COSTA_RICA,
                category = TvCategory.NOTICIAS,
                streamUrl = "https://extratv.streamlock.net/live/extratv42/playlist.m3u8",
                backupStreamUrls = listOf(
                    "https://5a7c5c2d33451.streamlock.net/costarica13/live/playlist.m3u8",
                    "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4"
                ),
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
                id = "cr_teletica_7",
                number = 7,
                name = "Teletica 7 (Tica TV)",
                country = Country.COSTA_RICA,
                category = TvCategory.ENTRETENIMIENTO,
                streamUrl = "https://teletica-live.akamaized.net/hls/live/2034909/teletica/master.m3u8",
                backupStreamUrls = listOf(
                    "https://live.multimedios.cr/hls/canal8cr.m3u8",
                    "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/WeAreGoingOnBullrun.mp4"
                ),
                logoText = "T7",
                brandColorHex = 0xFF00C853,
                description = "La señal preferida de Costa Rica: Telenoticias, 7 Días, Tu Cara Me Suena, toros a la tica y fútbol internacional.",
                broadcastQuality = "FHD 1080p",
                schedule = listOf(
                    ProgramItem("cr_t7_1", "Buen Día Costa Rica", "El programa de la mañana con temas de salud, jardinería, superación y optimismo.", TvCategory.ENTRETENIMIENTO, "08:00", "10:30", 480, 630, "TP", "Nancy Dobles"),
                    ProgramItem("cr_t7_2", "De Boca en Boca", "Los chismes, entrevistas exclusivas y la movida de las estrellas ticas.", TvCategory.ENTRETENIMIENTO, "10:30", "12:00", 630, 720, "TP", "Bismarck Méndez"),
                    ProgramItem("cr_t7_3", "Telenoticias Edición Mediodía", "El noticiero líder de la televisión costarricense con credibilidad comprobada.", TvCategory.NOTICIAS, "12:00", "14:00", 720, 840, "+14", "Ignacio Santos"),
                    ProgramItem("cr_t7_4", "Novela de la Tarde", "Grandes producciones dramáticas internacionales para emocionar al público.", TvCategory.ENTRETENIMIENTO, "14:00", "16:30", 840, 990, "+14"),
                    ProgramItem("cr_t7_5", "La Media Docena & Humor Tico", "Comedia sana con personajes populares de las tradiciones costarricenses.", TvCategory.ENTRETENIMIENTO, "16:30", "19:00", 990, 1140, "TP"),
                    ProgramItem("cr_t7_6", "Telenoticias Central", "El recuento minucioso de las principales noticias de Costa Rica y el orbe.", TvCategory.NOTICIAS, "19:00", "21:00", 1140, 1260, "+14"),
                    ProgramItem("cr_t7_7", "7 Días: Reportajes de Fondo", "Investigación periodística de alto calibre con denuncias y análisis.", TvCategory.NOTICIAS, "21:00", "22:30", 1260, 1350, "+14", "Rodolfo González"),
                    ProgramItem("cr_t7_8", "Zona de Gol Tica", "Todos los goles de la Liga Promerica costarricense y la Selección Nacional.", TvCategory.DEPORTES, "22:30", "24:00", 1350, 1440, "TP")
                )
            ),
            Channel(
                id = "cr_san_carlos_tv",
                number = 14,
                name = "San Carlos TV & Norte",
                country = Country.COSTA_RICA,
                category = TvCategory.CULTURA,
                streamUrl = "https://tvn14.streamlock.net/live/tvn14/playlist.m3u8",
                backupStreamUrls = listOf(
                    "https://5a7c5c2d33451.streamlock.net/costarica13/live/playlist.m3u8",
                    "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/WhatCarCanYouGetForAGrand.mp4"
                ),
                logoText = "SC",
                brandColorHex = 0xFF0097A7,
                description = "Voz y sentir de la Zona Norte de Costa Rica. Ganadería, agricultura, ecoturismo en el Volcán Arenal y tradiciones locales.",
                broadcastQuality = "HD 720p",
                schedule = listOf(
                    ProgramItem("cr_sc_1", "Amanecer Norteño", "Noticias del campo, ferias del agricultor y estado del clima en San Carlos.", TvCategory.NOTICIAS, "06:00", "08:30", 360, 510, "TP"),
                    ProgramItem("cr_sc_2", "Tradición Sancarleña", "Historias de los pioneros, rodeo campesino y exposiciones ganaderas.", TvCategory.CULTURA, "08:30", "11:30", 510, 690, "TP"),
                    ProgramItem("cr_sc_3", "Noticiero Regional Norte", "Información detallada de Alajuela, San Carlos, Upala y Los Chiles.", TvCategory.NOTICIAS, "11:30", "13:30", 690, 810, "TP"),
                    ProgramItem("cr_sc_4", "Ecoturismo Arenal", "Recorridos por senderos, aguas termales y biodiversidad de la llanura norteña.", TvCategory.CULTURA, "13:30", "16:00", 810, 960, "TP"),
                    ProgramItem("cr_sc_5", "Fútbol Regional Tico", "Partidos de los Torneos Cantonales y el equipo de la Asociación Deportiva San Carlos.", TvCategory.DEPORTES, "16:00", "18:30", 960, 1110, "TP"),
                    ProgramItem("cr_sc_6", "Noticias de la Noche Norteña", "Resumen de los hechos comunitarios de la jornada.", TvCategory.NOTICIAS, "18:30", "21:00", 1110, 1260, "TP"),
                    ProgramItem("cr_sc_7", "Noche Campesina y Folclor", "Música ranchera, trova y anécdotas de la Costa Rica rural.", TvCategory.MUSICA, "21:00", "24:00", 1260, 1440, "TP")
                )
            )
        )
    }
}
