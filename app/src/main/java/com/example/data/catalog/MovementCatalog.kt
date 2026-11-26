package com.example.data.catalog

import com.example.domain.model.*

object MovementCatalog {

    val disciplines: List<DisciplineInfo> = CatalogDisciplines.all

    val allMovements: List<MovementDetail> by lazy {
        CatalogPart1YogaCalisthenics.movements +
            CatalogPart2StrengthOlympic.movements +
            CatalogPart3CardioSprint.movements +
            CatalogPart4MobilityPrehab.movements +
            CatalogPart5CoreBalanceMartialBreathing.movements
    }

    private val byIdMap: Map<String, MovementDetail> by lazy {
        allMovements.associateBy { it.id }
    }

    fun getById(id: String): MovementDetail? = byIdMap[id]

    fun getDisciplineInfo(id: DisciplineId): DisciplineInfo = CatalogDisciplines.byId(id)

    fun forDiscipline(disciplineId: DisciplineId): Pair<List<MovementDetail>, List<MovementDetail>> {
        val list = allMovements.filter { it.discipline == disciplineId }
        return list.filter { it.category == MovementCategory.SIGNATURE_ADVANCED } to
            list.filter { it.category == MovementCategory.TRANSFERABLE_FUNCTIONAL }
    }

    fun searchAndFilter(
        query: String = "",
        discipline: DisciplineId? = null,
        category: MovementCategory? = null,
        difficulty: DifficultyLevel? = null,
        bodyRegion: BodyRegion? = null,
        noEquipmentOnly: Boolean = false,
        favoritesOnly: Boolean = false,
        favoriteIds: Set<String> = emptySet()
    ): List<MovementDetail> {
        val q = query.trim().lowercase()
        return allMovements.filter { m ->
            val matchesQuery = q.isEmpty() ||
                m.name.lowercase().contains(q) ||
                m.alternativeNames.any { it.lowercase().contains(q) } ||
                m.discipline.title.lowercase().contains(q) ||
                m.discipline.name.lowercase().contains(q) ||
                m.trainingPurpose.lowercase().contains(q) ||
                m.targetMuscles.any { it.lowercase().contains(q) } ||
                m.bodyRegions.any { it.label.lowercase().contains(q) || it.name.lowercase().contains(q) } ||
                m.primaryAbilities.any { it.lowercase().contains(q) } ||
                m.movementPattern.label.lowercase().contains(q) ||
                m.equipment.any { it.lowercase().contains(q) }

            val matchesDisc = discipline == null || m.discipline == discipline
            val matchesCat = category == null || m.category == category
            val matchesDiff = difficulty == null || m.difficulty == difficulty
            val matchesRegion = bodyRegion == null || bodyRegion in m.bodyRegions
            val matchesNoEquip = !noEquipmentOnly || m.equipment.any {
                it.contains("बिना", ignoreCase = true) ||
                    it.contains("कोई नहीं", ignoreCase = true) ||
                    it.contains("शरीर", ignoreCase = true) ||
                    it.contains("चटाई", ignoreCase = true) ||
                    it.contains("दीवार", ignoreCase = true) ||
                    it.contains("जमीन", ignoreCase = true) ||
                    it.contains("फर्श", ignoreCase = true) ||
                    it.contains("None", ignoreCase = true) ||
                    it.contains("Bodyweight", ignoreCase = true) ||
                    it.contains("Floor", ignoreCase = true) ||
                    it.contains("Wall", ignoreCase = true)
            }
            val matchesFav = !favoritesOnly || m.id in favoriteIds

            matchesQuery && matchesDisc && matchesCat && matchesDiff && matchesRegion && matchesNoEquip && matchesFav
        }
    }
}
