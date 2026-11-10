const { test, before, after, beforeEach } = require("node:test");
const assert = require("node:assert");
const fs = require("node:fs");

test("firestore.rules exists and contains valid user-scoped isolation", () => {
  const rules = fs.readFileSync("firestore.rules", "utf8");
  assert.ok(rules.includes("request.auth.uid == userId"), "Rules must enforce user isolation");
  assert.ok(rules.includes("isValidCard"), "Rules must validate card schema");
  assert.ok(rules.includes("isValidThread"), "Rules must validate thread schema");
});
