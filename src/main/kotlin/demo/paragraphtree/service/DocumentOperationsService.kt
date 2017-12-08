package demo.paragraphtree.service

import demo.paragraphtree.model.Document
import demo.paragraphtree.repository.DocumentRepository
import demo.paragraphtree.repository.find
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * @author Dmitry Zhuravlev
 *         Date:  06.12.2017
 */
@Service
@Transactional
class DocumentOperationsService : IDocumentOperationsService {
    @Autowired
    lateinit var documentRepository: DocumentRepository

    override fun getDocument(id: Long) = documentRepository.find(id)

    override fun createDocument() = documentRepository.save(Document())

    override fun updateDocument(document: Document) = documentRepository.save(document)

}