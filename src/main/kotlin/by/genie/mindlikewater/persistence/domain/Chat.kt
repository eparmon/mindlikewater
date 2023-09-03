package by.genie.mindlikewater.persistence.domain

import jakarta.persistence.*

@Entity
@Table(name = "chats")
class Chat() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null

    @Column(name = "external_id")
    var externalId: Long? = null

    @Column(name = "active_command")
    var activeCommand: String? = null

    @Column
    var context: String? = null

    constructor(externalId: Long?) : this() {
        this.externalId = externalId
    }

}
