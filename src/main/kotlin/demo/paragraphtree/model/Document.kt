package demo.paragraphtree.model

import org.neo4j.ogm.annotation.GeneratedValue
import org.neo4j.ogm.annotation.Id
import org.neo4j.ogm.annotation.NodeEntity
import org.neo4j.ogm.annotation.Relationship

/**
 * @author Dmitry Zhuravlev
 *         Date:  01.12.2017
 */
@NodeEntity
data class Document(
        @Id
        @GeneratedValue
        var id: Long? = null,
        @Relationship(type = "FIRST_PARAGRAPH", direction = Relationship.OUTGOING)
        var firstParagraph: Paragraph? = null
)