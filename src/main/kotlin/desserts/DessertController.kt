package desserts

import io.javalin.apibuilder.CrudHandler
import io.javalin.http.Context
import java.util.concurrent.CompletableFuture

class DessertController: CrudHandler{
    override fun create(ctx: Context) {
        ctx.result(CompletableFuture.supplyAsync{DessertService.insert(ctx)})
    }

    override fun delete(ctx: Context, resourceId: String) {
        ctx.result(CompletableFuture.supplyAsync{DessertService.delete(resourceId)})
    }

    override fun getAll(ctx: Context) {
        ctx.json(CompletableFuture.supplyAsync{DessertService.selectAll(ctx)})
    }

    override fun getOne(ctx: Context, resourceId: String) {
        ctx.json(CompletableFuture.supplyAsync{DessertService.selectById(resourceId)})
    }

    override fun update(ctx: Context, resourceId: String) {
        ctx.result(CompletableFuture.supplyAsync{DessertService.update(ctx,resourceId)})
    }




}

