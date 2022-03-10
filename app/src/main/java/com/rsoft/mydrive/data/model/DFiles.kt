package com.rsoft.mydrive.data.model

data class DFiles(
    var kind: String? = null,
    var nextPageToken: String? = null,
    var files: List<DFile>
)
