package cr.ac.una.LocationWiki.controller

import cr.ac.una.LocationWiki.clases.page
import cr.ac.una.LocationWiki.service.PagesService

class PageController {
    var pagesService = PagesService()

    suspend fun  Buscar(terminoBusqueda: String):ArrayList<page>{
        return pagesService.apiWikiService.Buscar(terminoBusqueda).pages as ArrayList<page>
    }
}