package com.ssg.ic.sp.ktor.com.ktorm

import com.ssg.ic.sp.db.CRUDRepository
import com.ssg.ic.sp.db.model.Entity

abstract class KtormCRUDRepository<T : Entity<ID>, ID>(database: KtormDatabase) : KtormRepository(database) , CRUDRepository<T, ID>