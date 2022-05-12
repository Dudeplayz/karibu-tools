package com.github.mvysny.kaributools

import com.github.mvysny.dynatest.DynaTest
import kotlin.test.expect

private val String.v: SemanticVersion get() = SemanticVersion.fromString(this)

class SemanticVersionTest : DynaTest({
    test("toString") {
        expect("1.2.3") { SemanticVersion(1, 2, 3).toString() }
        expect("1.2.3-beta2") { SemanticVersion(1, 2, 3, "beta2").toString() }
        expect("14.2.3-SNAPSHOT") { SemanticVersion(14, 2, 3, "SNAPSHOT").toString() }
    }
    test("fromString") {
        expect(SemanticVersion(1, 2, 3)) { "1.2.3".v }
        expect("1.2.3-beta2") { "1.2.3-beta2".v.toString() }
        expect("1.2.3-beta2") { "1.2.3.beta2".v.toString() }
        expect("1.2.3-SNAPSHOT") { "1.2.3-SNAPSHOT".v.toString() }
        expect("1.2.0-SNAPSHOT") { "1.2-SNAPSHOT".v.toString() }
    }
    test("compare to") {
        expect(true) { "14.2.28".v < "14.3.0".v }
        expect(true) { "14.3.0".v < "15.0.0".v }
        expect(true) { "16.0.2".v < "17.0.0".v }
        expect(true) { "14.3.0-beta2".v < "14.3.0".v }
        expect(true) { "1.2-SNAPSHOT".v < "1.2".v }
        expect(true) { "1.2-SNAPSHOT".v < "1.2.0".v }
        expect(true) { "1.2.0-SNAPSHOT".v < "1.2.0".v }
    }
    test("is at least major,minor") {
        expect(true) { SemanticVersion(14, 3, 0).isAtLeast(14, 2) }
        expect(true) { SemanticVersion(14, 3, 0).isAtLeast(14, 3) }
        expect(false) { SemanticVersion(14, 3, 0).isAtLeast(14, 4) }
        expect(true) { SemanticVersion(14, 3, 1).isAtLeast(14, 2) }
        expect(true) { SemanticVersion(14, 3, 1).isAtLeast(14, 3) }
        expect(true) { SemanticVersion(14, 3, 0, "alpha1").isAtLeast(14, 2) }
        expect(true) { SemanticVersion(14, 3, 0, "alpha1").isAtLeast(14, 3) }
    }
    test("is at least major") {
        expect(true) { SemanticVersion(14, 3, 0).isAtLeast(14) }
        expect(true) { SemanticVersion(14, 3, 0).isAtLeast(13) }
        expect(false) { SemanticVersion(14, 3, 0).isAtLeast(15) }
        expect(true) { SemanticVersion(14, 3, 1).isAtLeast(14) }
        expect(true) { SemanticVersion(14, 3, 1).isAtLeast(13) }
        expect(true) { SemanticVersion(14, 3, 0, "alpha1").isAtLeast(14) }
        expect(true) { SemanticVersion(14, 3, 0, "alpha1").isAtLeast(13) }
    }
    test("is exactly major") {
        expect(true) { SemanticVersion(14, 3, 0).isExactly(14) }
        expect(false) { SemanticVersion(14, 3, 0).isExactly(13) }
        expect(false) { SemanticVersion(14, 3, 0).isExactly(15) }
        expect(true) { SemanticVersion(14, 3, 1).isExactly(14) }
        expect(false) { SemanticVersion(14, 3, 1).isExactly(13) }
        expect(true) { SemanticVersion(14, 3, 0, "alpha1").isExactly(14) }
        expect(false) { SemanticVersion(14, 3, 0, "alpha1").isExactly(13) }
    }
    test("is exactly major,minor") {
        expect(true) { SemanticVersion(14, 3, 0).isExactly(14, 3) }
        expect(false) { SemanticVersion(14, 3, 0).isExactly(14, 2) }
        expect(false) { SemanticVersion(14, 3, 0).isExactly(15, 0) }
        expect(true) { SemanticVersion(14, 3, 1).isExactly(14, 3) }
        expect(false) { SemanticVersion(14, 3, 1).isExactly(14, 2) }
        expect(true) { SemanticVersion(14, 3, 0, "alpha1").isExactly(14, 3) }
        expect(false) { SemanticVersion(14, 3, 0, "alpha1").isExactly(14, 2) }
    }
})
