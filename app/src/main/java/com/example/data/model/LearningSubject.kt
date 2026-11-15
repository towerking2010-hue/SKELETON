package com.example.data.model

/**
 * The permanent SKELETON knowledge taxonomy.
 * Classification is derived from existing card metadata, so no Room migration is required.
 */
enum class LearningSubject(val label: String, private val keywords: List<String>) {
  ALL("All", emptyList()),
  PHYSICS("Physics", listOf("physics", "mechanics", "thermodynamic", "electromagnetic", "quantum", "relativity", "optics")),
  CHEMISTRY("Chemistry", listOf("chemistry", "chemical", "molecule", "molecular", "reaction", "periodic table", "bonding")),
  BIOLOGY("Biology", listOf("biology", "biological", "cellular", "genetic", "evolution", "anatomy", "physiology", "ecology")),
  ETYMOLOGY_LINGUISTICS("Etymology & Linguistic Software", listOf("etymology", "linguistic", "language", "grammar", "phonetic", "morphology", "syntax")),
  MYTHOLOGY_ARCHETYPES("Mythology & Comparative Archetypes", listOf("mythology", "myth", "archetype", "greek", "egyptian", "norse", "hindu", "buddhist")),
  SACRED_GEOMETRY("Sacred Geometry & Universal Ratios", listOf("sacred geometry", "fibonacci", "golden ratio", "phi", "platonic solid", "fractal")),
  NUMEROLOGY("Numerology", listOf("numerology", "life path number", "number symbolism")),
  GEMATRIA("Gematria", listOf("gematria", "isopsephy", "abjad")),
  ALCHEMY("Alchemy & Psychological Transmutation", listOf("alchemy", "alchemical", "transmutation", "prima materia", "philosopher's stone")),
  TRIVIUM("Trivium", listOf("trivium", "grammar", "logic", "rhetoric")),
  QUADRIVIUM("Quadrivium", listOf("quadrivium", "arithmetic", "geometry", "music theory", "astronomy")),
  FREEMASONRY("Freemasonry & Architectural Allegory", listOf("freemasonry", "masonic", "masonry", "architectural allegory", "symbolic architecture")),
  HERMETICISM("Hermeticism & Universal Laws", listOf("hermetic", "hermetism", "principle of correspondence", "principle of vibration")),
  METAPHYSICS("Metaphysics & Ontology", listOf("metaphysics", "ontology", "epistemology", "being", "existence")),
  MUSIC_ACOUSTICS("Music Theory & Acoustic Harmonics", listOf("music theory", "acoustic", "harmonic", "frequency", "sound wave", "musical")),
  COLOUR_WAVES("Colour Theory & Wave Spectra", listOf("colour theory", "color theory", "wave spectra", "wavelength", "spectrum", "spectrometry")),
  VISUAL_COMPOSITION("Visual Composition & Spatial Balance", listOf("visual composition", "spatial balance", "composition", "visual design", "perspective")),
  PSYCHOLOGY("General Psychology & Cognitive Science", listOf("psychology", "cognitive", "behavior", "behaviour", "neuroscience", "memory", "attention")),
  DARK_PSYCHOLOGY("Dark Psychology, Manipulation & Defense Mechanics", listOf("dark psychology", "manipulation", "coercion", "persuasion", "defense mechanics", "social engineering")),
  SCIENCE("Science", listOf("science", "scientific")),
  MATHEMATICS("Mathematics", listOf("math", "calculus", "algebra", "number theory")),
  HISTORY("History", listOf("history", "historical", "civilization", "ancient", "empire", "archaeology")),
  TECHNOLOGY_AI("Technology & AI", listOf("technology", "computer", "software", "programming", "artificial intelligence", "machine learning", "network", "blockchain", "data")),
  BUSINESS_ECONOMICS("Business & Economics", listOf("business", "economics", "finance", "market", "capital", "trade")),
  ASTRONOMY("Astronomy", listOf("astronomy", "astrophysics", "cosmos", "universe", "planet", "stellar", "space")),
  PHILOSOPHY("Philosophy", listOf("philosophy", "ethics")),
  ARTS_DESIGN("Arts & Design", listOf("music", "colour", "color", "design", "art"));

  fun matches(card: FeedCard): Boolean {
    if (this == ALL) return true
    val searchable = "${card.title} ${card.branchDomain} ${card.crossLinkTarget.orEmpty()}".lowercase()
    return keywords.any { keyword -> searchable.contains(keyword) }
  }
}
