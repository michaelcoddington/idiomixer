import com.orientechnologies.orient.core.db.OrientDB
import com.orientechnologies.orient.core.db.OrientDBConfig
import com.orientechnologies.orient.core.record.ODirection

object Test1 {
}

fun main(args: Array<String>) {
    val orient = OrientDB("remote:localhost", OrientDBConfig.defaultConfig())
    println("Got db $orient")
    val dbSession = orient.open("test-1", "root", "admin")

    println(dbSession.metadata.schema.classes)

    // creates a new vertex class
    val assetClass = dbSession.metadata.schema.getClass("Asset")
    if (assetClass == null) {
        val vClass = dbSession.metadata.schema.getClass("V")
        dbSession.metadata.schema.createClass("Asset", vClass)
    } else {
        println("Asset class already exists")
    }



    val start = System.currentTimeMillis()
    //val query = "TRAVERSE inE(), outE(), inV(), outV from (SELECT * from Product)"
    val query = "SELECT FROM V"
    val results = dbSession.query(query)
    println("Got results $results in ${System.currentTimeMillis() - start} ms")
    results.forEach { result ->

        println(result)
        if (result.isVertex) {
            val v = result.vertex
            println(v)
            val contribs = v.get().getEdges(ODirection.OUT, "HAS_CONTRIBUTOR")
            contribs.forEach { contributorLink ->
                println("got link $contributorLink")
                val contributor = contributorLink.to
                println("got contributor $contributor")
            }
        }

    }
    orient.close()
}

