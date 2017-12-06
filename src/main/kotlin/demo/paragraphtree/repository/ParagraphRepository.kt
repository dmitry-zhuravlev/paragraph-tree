package demo.paragraphtree.repository

import demo.paragraphtree.model.Paragraph
import org.springframework.data.neo4j.annotation.Query
import org.springframework.data.neo4j.repository.Neo4jRepository
import org.springframework.data.repository.query.Param

/**
 * @author Dmitry Zhuravlev
 *         Date:  01.12.2017
 */
interface ParagraphRepository : Neo4jRepository<Paragraph, Long> {

    @Query("MATCH (prev:Paragraph)-[:NEXT]->(next:Paragraph) WHERE ID(next) = {currentParagraphId} RETURN prev")
    fun findPreviousParagraph(@Param("currentParagraphId") currentParagraphId: Long): Paragraph?
}

fun ParagraphRepository.find(id: Long, depth: Int = -1) = findById(id, depth).orElse(null)