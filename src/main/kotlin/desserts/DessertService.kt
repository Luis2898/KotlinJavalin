package desserts

import io.javalin.http.Context
import  kotliquery.*
import  java.util.*

data class Dessert(
    var id: String? = null,
    val name: String? = null,
    val calories: Double? = 0.0,
    val fat: Double? = 0.0,
    val carbs: Double? = 0.0,
    val protein: Double? = 0.0
)

object DessertService {

    fun insert(ctx: Context): String {
        val id = UUID.randomUUID().toString()
        val dessert = ctx.bodyValidator<Dessert>().get()
        var result = "failed"
        val qryIns = queryOf(
            "INSERT INTO DESSERT (ID,NAME,CALORIES,FAT,CARBS,PROTEIN) VALUES (CHAR_TO_UUID(?),?,?,?,?,?)".trimIndent(),
            id, dessert.name, dessert.calories, dessert.fat, dessert.carbs, dessert.protein
        )
        using(sessionOf(HikariCP.dataSource())) {

            result = if (it.run(qryIns.asUpdate) > 0) id else "failed"

        }
        return result
    }

    fun delete(resourceId: String): String {
        val qryDel = queryOf(" DELETE FROM DESSERT WHERE ID =CHAR_TO_UUID(?)".trimIndent(), resourceId);
        var result = "failed"
        using(
            sessionOf(HikariCP.dataSource())
        ) {

            result = if (it.run(qryDel.asUpdate) > 0) "succes" else "failed"
        }
        return result
    }

    private val formResultset: (Row) -> Dessert = { row ->
        Dessert(
            row.string("ID"),
            row.string("NAME"),
            row.double("CALORIES"),
            row.double("FAT"),
            row.double("CARBS"),
            row.double("PROTEIN")
        )
    }

    fun selectAll(ctx:Context): List<Dessert> {
        val qryAll =
            queryOf("SELECT UUID_TO_CHAR(ID) AS ID,NAME,CALORIES,FAT,CARBS,PROTEIN FROM DESSERT ORDER BY NAME ".trimIndent())
                .map { formResultset(it) }.asList
        var result = listOf<Dessert>()
        using(sessionOf(HikariCP.dataSource())) {

            result = it.run(qryAll)


        }
        print("MI RESULT")
        print(result)
        return result

    }

    fun selectById(resourceId: String): Dessert {
        val qrySel = queryOf(
            "SELECT UUID_TO_CHAR(ID) AS ID,NAME,CALORIES,FAT,CARBS,PROTEIN FROM DESSERT WHERE ID= CHAR_TO_UUID(?)"
                .trimIndent(), resourceId
        ).map(formResultset).asSingle
        var result = Dessert()
        using(sessionOf(HikariCP.dataSource())) {

            result = it.run(qrySel) ?: Dessert()

        }
        return result
    }

    fun update(ctx: Context, resourceId: String): String {
        val dessert = ctx.bodyValidator<Dessert>().get()
        val qryUpd = queryOf(
            " UPDATE DESSERT SET NAME= ? ,CALORIES=?, FAT=?,CARBS=?,PROTEIN=? WHERE ID= CHAR_TO_UUID(?) ".trimIndent(),
            dessert.name, dessert.calories, dessert.fat, dessert.carbs, dessert.protein, resourceId
        )
        var result = "failed"
        using(
            sessionOf(HikariCP.dataSource())
        ) {

            result = if (it.run(qryUpd.asUpdate) > 0) "success" else "failed"

        }
        return result
    }

}


