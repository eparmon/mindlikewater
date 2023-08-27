package by.genie.mindlikewater.persistence.domain

import jakarta.persistence.*
import java.math.BigDecimal

@Entity
@Table(name = "foods")
class Food {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null

    var name: String? = null

    var raw: Boolean? = false

    var energy: BigDecimal? = null

    var water: BigDecimal? = null

    var carbs: BigDecimal? = null

    var fats: BigDecimal? = null

    var proteins: BigDecimal? = null

    var omega3: BigDecimal? = null

    var omega6: BigDecimal? = null

    @Column(name = "vitamin_b1")
    var vitaminB1: BigDecimal? = null

    @Column(name = "vitamin_b2")
    var vitaminB2: BigDecimal? = null

    @Column(name = "vitamin_b3")
    var vitaminB3: BigDecimal? = null

    @Column(name = "vitamin_b5")
    var vitaminB5: BigDecimal? = null

    @Column(name = "folate")
    var folate: BigDecimal? = null

    @Column(name = "vitamin_a")
    var vitaminA: BigDecimal? = null

    @Column(name = "vitamin_e")
    var vitaminE: BigDecimal? = null

    @Column(name = "vitamin_k")
    var vitaminK: BigDecimal? = null

    var calcium: BigDecimal? = null

    var iron: BigDecimal? = null

    var magnesium: BigDecimal? = null

    var phosphorus: BigDecimal? = null

    var selenium: BigDecimal? = null

    var zinc: BigDecimal? = null

}
