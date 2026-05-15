package com.example.voicemind.domain.game

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class WordChainGame {

    // Danh sách từ hợp lệ (mặc định, có thể mở rộng)
    private val defaultWordSet = setOf(
        // Cơ bản (A-Z)
        "apple", "ant", "air", "all", "able", "about", "above", "accept", "across", "act",
        "active", "actual", "add", "address", "admit", "adult", "advice", "affect", "afraid", "after",
        "again", "age", "agent", "ago", "agree", "ahead", "aim", "air", "airport", "alive",
        "all", "allow", "almost", "alone", "along", "already", "also", "although", "always", "among",
        "amount", "and", "anger", "animal", "another", "answer", "any", "apart", "appear", "apple",
        "area", "arm", "army", "around", "arrange", "arrive", "art", "article", "artist", "as",
        "ask", "assume", "at", "attack", "attempt", "attend", "attention", "attract", "author", "available",
        "avoid", "away", "baby", "back", "bad", "bag", "balance", "ball", "band", "bank",
        "bar", "base", "basic", "basis", "beat", "beauty", "because", "become", "bed", "been",
        "before", "begin", "behave", "behind", "believe", "bell", "belong", "below", "belt", "benefit",
        "best", "better", "between", "beyond", "big", "bird", "birth", "bit", "black", "block",
        "blood", "blow", "blue", "board", "boat", "body", "book", "border", "both", "bottle",
        "bottom", "box", "boy", "brain", "branch", "break", "breath", "bridge", "brief", "bright",
        "bring", "broad", "broken", "brother", "brown", "brush", "budget", "build", "building", "burn",
        "bus", "business", "busy", "but", "buy", "by", "cake", "call", "camera", "camp",
        "can", "car", "card", "care", "career", "carry", "case", "cat", "catch", "cause",
        "cell", "center", "century", "certain", "chair", "challenge", "chance", "change", "character", "charge",
        "check", "child", "choice", "choose", "church", "circle", "citizen", "city", "civil", "claim",
        "class", "clean", "clear", "click", "climb", "clock", "close", "clothes", "cloud", "club",
        "coach", "coast", "code", "coffee", "cold", "collection", "college", "color", "come", "commercial",
        "common", "company", "compare", "computer", "concern", "condition", "conference", "consider", "consumer", "contain",
        "continue", "control", "cook", "cool", "copy", "corner", "corporate", "cost", "could", "count",
        "country", "course", "court", "cover", "create", "crime", "cultural", "cup", "current", "customer",
        "cut", "daily", "damage", "dance", "danger", "dark", "data", "date", "daughter", "day",
        "dead", "deal", "death", "debate", "decade", "decide", "decision", "deep", "defense", "degree",
        "delay", "deliver", "demand", "democratic", "describe", "design", "despite", "detail", "determine", "develop",
        "die", "difference", "difficult", "digital", "direct", "direction", "discover", "discuss", "disease", "district",
        "divide", "doctor", "dog", "door", "double", "doubt", "down", "draw", "dream", "dress",
        "drink", "drive", "drop", "drug", "dry", "due", "during", "duty", "each", "early",
        "earth", "easily", "east", "easy", "eat", "economic", "edge", "education", "effect", "efficient",
        "effort", "eight", "either", "election", "electric", "element", "else", "email", "employ", "empty",
        "end", "enemy", "energy", "engine", "enjoy", "enough", "enter", "environment", "equal", "error",
        "escape", "especially", "essential", "establish", "even", "evening", "event", "ever", "every", "everyone",
        "evidence", "exact", "example", "excellent", "except", "exchange", "exist", "expect", "experience", "expert",
        "explain", "express", "extend", "extra", "eye", "face", "fact", "factor", "fail", "fall",
        "family", "far", "fast", "father", "fear", "feed", "feel", "female", "few", "field",
        "fight", "figure", "file", "fill", "film", "final", "financial", "find", "fine", "finger",
        "finish", "fire", "firm", "first", "fish", "fit", "five", "floor", "fly", "focus",
        "follow", "food", "foot", "for", "force", "foreign", "forget", "form", "former", "forward",
        "four", "free", "friend", "from", "front", "full", "fun", "function", "fund", "future",
        "game", "garden", "gas", "general", "get", "girl", "give", "glass", "go", "goal",
        "god", "gold", "good", "government", "great", "green", "ground", "group", "grow", "guess",
        "gun", "hair", "half", "hand", "hang", "happen", "happy", "hard", "have", "head",
        "health", "hear", "heart", "heat", "heavy", "help", "her", "here", "high", "history",
        "hit", "hold", "home", "hope", "horse", "hospital", "hot", "hotel", "hour", "house",
        "how", "huge", "human", "idea", "identify", "if", "image", "imagine", "impact", "important",
        "improve", "in", "include", "increase", "indeed", "indicate", "individual", "industry", "information", "inside",
        "instead", "institution", "interest", "international", "interview", "into", "investment", "involve", "issue", "it",
        "item", "job", "join", "jump", "just", "keep", "key", "kill", "kind", "kitchen",
        "know", "land", "language", "large", "last", "late", "laugh", "law", "lay", "lead",
        "learn", "leave", "left", "less", "let", "letter", "level", "lie", "life", "light",
        "like", "likely", "line", "list", "listen", "little", "live", "load", "local", "lock",
        "long", "look", "lord", "lose", "loss", "lot", "love", "low", "luck", "lunch",
        "machine", "main", "major", "make", "man", "manage", "many", "market", "match", "matter",
        "may", "me", "mean", "measure", "media", "medical", "meet", "member", "memory", "mention",
        "message", "method", "middle", "might", "mile", "military", "milk", "million", "mind", "minute",
        "miss", "model", "moment", "money", "month", "more", "morning", "most", "mother", "move",
        "movie", "much", "music", "must", "my", "name", "nation", "natural", "nature", "near",
        "necessary", "need", "network", "never", "new", "news", "next", "nice", "night", "no",
        "none", "nor", "north", "not", "note", "nothing", "notice", "now", "number", "occur",
        "of", "off", "offer", "office", "often", "oil", "ok", "old", "on", "once",
        "one", "only", "open", "operation", "opportunity", "option", "or", "order", "organization", "other",
        "out", "outside", "over", "own", "owner", "page", "pain", "paper", "parent", "part",
        "particular", "party", "pass", "past", "path", "patient", "pay", "peace", "people", "per",
        "perform", "perhaps", "period", "person", "phone", "photo", "physical", "pick", "picture", "piece",
        "place", "plan", "plant", "play", "please", "point", "police", "political", "poor", "position",
        "positive", "possible", "post", "power", "practice", "prepare", "present", "press", "pretty", "price",
        "private", "probably", "problem", "process", "produce", "product", "program", "project", "property", "prove",
        "provide", "public", "pull", "purpose", "push", "put", "quality", "question", "quick", "quiet",
        "quite", "race", "radio", "raise", "range", "rate", "rather", "reach", "read", "ready",
        "real", "reality", "reason", "receive", "recent", "record", "red", "reduce", "reflect", "region",
        "relate", "relationship", "religious", "remain", "remember", "remove", "report", "represent", "require", "research",
        "resource", "respond", "response", "rest", "result", "return", "reveal", "rich", "right", "rise",
        "risk", "road", "rock", "role", "room", "rule", "run", "safe", "same", "save",
        "say", "scene", "school", "science", "score", "sea", "season", "seat", "second", "section",
        "security", "see", "seek", "seem", "sell", "send", "sense", "series", "serious", "serve",
        "service", "set", "seven", "sexual", "shake", "share", "she", "shoot", "short", "shot",
        "should", "show", "side", "sign", "significant", "similar", "simple", "simply", "since", "sing",
        "single", "sister", "sit", "situation", "six", "size", "skill", "skin", "small", "smile",
        "social", "society", "soft", "software", "soil", "son", "song", "soon", "sort", "sound",
        "source", "south", "space", "speak", "special", "specific", "spend", "sport", "spring", "staff",
        "stage", "stand", "standard", "star", "start", "state", "station", "stay", "step", "still",
        "stock", "stop", "store", "story", "straight", "strange", "street", "strength", "stress", "stretch",
        "strike", "string", "strong", "structure", "student", "study", "stuff", "style", "subject", "success",
        "such", "sudden", "suffer", "suggest", "summer", "sun", "supply", "support", "sure", "surface",
        "surprise", "surround", "survey", "survive", "sweet", "swim", "system", "table", "take", "talk",
        "task", "tax", "tea", "teach", "team", "technology", "television", "tell", "ten", "tend",
        "term", "test", "than", "thank", "that", "the", "their", "them", "then", "there",
        "these", "they", "thin", "thing", "think", "this", "those", "though", "thought", "thousand",
        "three", "through", "throw", "thus", "time", "to", "today", "together", "tomorrow", "tone",
        "too", "top", "total", "touch", "toward", "town", "track", "trade", "traditional", "train",
        "travel", "treat", "tree", "trial", "trip", "trouble", "true", "trust", "truth", "try",
        "tube", "turn", "two", "type", "under", "understand", "unit", "until", "up", "upon",
        "us", "use", "usual", "value", "various", "very", "view", "village", "visit", "voice",
        "wait", "walk", "wall", "want", "war", "watch", "water", "way", "we", "wear",
        "week", "weight", "welcome", "well", "west", "what", "when", "where", "whether", "which",
        "while", "white", "who", "whole", "why", "wide", "wife", "will", "win", "wind",
        "window", "wish", "with", "within", "without", "woman", "wonder", "word", "work", "world",
        "worry", "would", "write", "wrong", "year", "yes", "yesterday", "yet", "you", "young"
    )

    private var validWords: Set<String> = defaultWordSet

    fun getCurrentWordSet(): Set<String> = validWords

    fun updateDictionary(newWords: Set<String>) {
        validWords = newWords
    }

    private val _currentWord = MutableStateFlow("")
    val currentWord: StateFlow<String> = _currentWord.asStateFlow()

    private val _score = MutableStateFlow(0)
    val score: StateFlow<Int> = _score.asStateFlow()

    private val _chain = MutableStateFlow<List<String>>(emptyList())
    val chain: StateFlow<List<String>> = _chain.asStateFlow()

    private val _isGameOver = MutableStateFlow(false)
    val isGameOver: StateFlow<Boolean> = _isGameOver.asStateFlow()

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message.asStateFlow()

    fun startGame(startWord: String) {
        val word = startWord.lowercase().trim()
        require(validWords.contains(word)) { "Từ bắt đầu không có trong từ điển" }
        _currentWord.value = word
        _chain.value = listOf(word)
        _score.value = 0
        _isGameOver.value = false
        _message.value = null
    }

    fun submitWord(word: String): Boolean {
        if (_isGameOver.value) {
            _message.value = "Game over! Please start a new game."
            return false
        }

        val cleanWord = word.lowercase().trim()
        if (cleanWord.isEmpty()) {
            _message.value = "Please enter a word"
            return false
        }

        val lastChar = _currentWord.value.last()
        if (cleanWord.first() != lastChar) {
            _message.value = "Word must start with '${lastChar.uppercase()}'"
            return false
        }

        if (_chain.value.contains(cleanWord)) {
            _message.value = "You already used '$cleanWord'"
            return false
        }

        if (!validWords.contains(cleanWord)) {
            _message.value = "'$cleanWord' is not in our vocabulary list"
            return false
        }

        // Thành công
        _currentWord.value = cleanWord
        _chain.value = _chain.value + cleanWord
        val points = 10 + (_chain.value.size / 5) // thưởng thêm mỗi 5 từ
        _score.value += points
        _message.value = "✓ Great! +$points points"
        return true
    }

    fun endGame() {
        _isGameOver.value = true
        _message.value = "Game finished! Final score: ${_score.value}"
    }

    fun resetGame() {
        _isGameOver.value = false
        _message.value = null
        // Không reset chain – nên gọi startGame riêng
    }
}