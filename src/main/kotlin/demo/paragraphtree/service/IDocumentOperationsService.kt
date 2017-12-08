package demo.paragraphtree.service

import demo.paragraphtree.model.Document

/**
 * @author Dmitry Zhuravlev
 *         Date:  08.12.2017
 */
interface IDocumentOperationsService {
    fun getDocument(id: Long): Document?
    fun createDocument(): Document?
    fun updateDocument(document: Document): Document?
}