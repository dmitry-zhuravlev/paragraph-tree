package demo.paragraphtree.rest

import demo.paragraphtree.service.IDocumentOperationsService
import demo.paragraphtree.service.IParagraphOperationsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

/**
 * @author Dmitry Zhuravlev
 *         Date:  01.12.2017
 */
@RestController
class OperationsEndpoint {

    @Autowired
    lateinit var documentOperationsService: IDocumentOperationsService

    @Autowired
    lateinit var paragraphOperationsService: IParagraphOperationsService

    @RequestMapping(value = "moveParagraphBefore", method = arrayOf(RequestMethod.PUT))
    fun moveParagraphBefore(@RequestParam("paragraphId") paragraphId: Long,
                            @RequestParam("beforeParagraphId") beforeParagraphId: Long)
            = paragraphOperationsService.moveParagraphBefore(paragraphId, beforeParagraphId)

    @RequestMapping(value = "moveParagraphAfter", method = arrayOf(RequestMethod.PUT))
    fun moveParagraphAfter(@RequestParam("paragraphId") paragraphId: Long,
                           @RequestParam("afterParagraphId") afterParagraphId: Long)
            = paragraphOperationsService.moveParagraphAfter(paragraphId, afterParagraphId)

    @RequestMapping(value = "createParagraph", method = arrayOf(RequestMethod.POST))
    fun createParagraph(@RequestParam("paragraphText") paragraphText: String,
                        @RequestParam("documentId") documentId: Long,
                        @RequestParam("firstParagraphId", required = false) firstParagraphId: Long?,
                        @RequestParam("secondParagraphId", required = false) secondParagraphId: Long?)
            = paragraphOperationsService.insertParagraphBetween(documentId, paragraphText, firstParagraphId, secondParagraphId)


    @RequestMapping(value = "createDocument", method = arrayOf(RequestMethod.POST))
    fun createDocument() = documentOperationsService.createDocument()

    @RequestMapping(value = "getParagraph", method = arrayOf(RequestMethod.GET))
    fun getParagraph(@RequestParam("id") id: Long) = paragraphOperationsService.getParagraph(id)

    @RequestMapping(value = "getDocument", method = arrayOf(RequestMethod.GET))
    fun getDocument(@RequestParam("id") id: Long) = documentOperationsService.getDocument(id)

    @RequestMapping(value = "updateFirstParagraph", method = arrayOf(RequestMethod.PUT))
    fun updateFirstParagraph(@RequestParam("documentId") documentId: Long,
                             @RequestParam("paragraphId") paragraphId: Long): ResponseEntity<*> {
        val document = documentOperationsService.getDocument(documentId)
                ?: return ResponseEntity.badRequest().body("Document with id= $documentId should exist")
        val paragraph = paragraphOperationsService.getParagraph(paragraphId)
                ?: return ResponseEntity.badRequest().body("Paragraph with id= $paragraphId should exist")
        document.firstParagraph = paragraph
        return ResponseEntity.ok(documentOperationsService.updateDocument(document))
    }

    @RequestMapping(value = "updateParagraphText", method = arrayOf(RequestMethod.PUT))
    fun updateParagraphText(@RequestParam("paragraphId") paragraphId: Long,
                            @RequestParam("paragraphText") paragraphText: String)
            = paragraphOperationsService.updateParagraphText(paragraphId, paragraphText)

    @RequestMapping(value = "insertChildParagraph", method = arrayOf(RequestMethod.POST))
    fun insertChildParagraph(@RequestParam("documentId") documentId: Long,
                             @RequestParam("paragraphText") paragraphText: String,
                             @RequestParam("parentParagraphId") parentParagraphId: Long)
            = paragraphOperationsService.insertChildParagraph(documentId, paragraphText, parentParagraphId)
}