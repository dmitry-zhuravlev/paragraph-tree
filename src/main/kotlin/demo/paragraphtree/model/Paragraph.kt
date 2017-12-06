package demo.paragraphtree.model

import org.neo4j.ogm.annotation.GeneratedValue
import org.neo4j.ogm.annotation.Id
import org.neo4j.ogm.annotation.NodeEntity
import org.neo4j.ogm.annotation.Relationship

/**
 * @author Dmitry Zhuravlev
 *         Date:  30.11.2017
 */
@NodeEntity
data class Paragraph(
        @Id
        @GeneratedValue
        var id: Long? = null,

        var documentId: Long? = null,

        var text: String = "",

        @Relationship(type = "NEXT", direction = Relationship.OUTGOING)
        var next: Paragraph? = null,

        @Relationship(type = "CHILD", direction = Relationship.OUTGOING)
        var child: Paragraph? = null

)