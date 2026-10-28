const {
  initializeTestEnvironment,
  assertFails,
  assertSucceeds,
} = require("@firebase/rules-unit-testing");
const { test, before, after, beforeEach } = require("node:test");
const fs = require("node:fs");

let testEnv;
const PROJECT_ID = process.env.GCP_PROJECT || "demo-no-project";
const ALICE_UID = "alice_123";
const BOB_UID = "bob_456";

const [emulatorHost, emulatorPortStr] = (process.env.FIRESTORE_EMULATOR_HOST || "127.0.0.1:8085").split(":");
const emulatorPort = parseInt(emulatorPortStr, 10);

before(async () => {
  const rules = fs.readFileSync("./firestore.rules", "utf8");
  testEnv = await initializeTestEnvironment({
    projectId: PROJECT_ID,
    firestore: {
      rules,
      host: emulatorHost,
      port: emulatorPort,
    },
  });
});

after(async () => {
  if (testEnv) {
    await testEnv.cleanup();
  }
});

beforeEach(async () => {
  if (testEnv) {
    await testEnv.clearFirestore();
  }
});

test("unauthenticated user cannot read or write user profiles", async () => {
  const unauthedDb = testEnv.unauthenticatedContext().firestore();
  await assertFails(unauthedDb.doc(`users/${ALICE_UID}`).get());
  await assertFails(
    unauthedDb.doc(`users/${ALICE_UID}`).set({
      uid: ALICE_UID,
      email: "alice@example.com",
      displayName: "Alice",
      createdAt: new Date(),
      lastActive: new Date(),
    })
  );
});

test("authenticated user can create and read their own profile", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  await assertSucceeds(
    aliceDb.doc(`users/${ALICE_UID}`).set({
      uid: ALICE_UID,
      email: "alice@example.com",
      displayName: "Alice",
      createdAt: new Date(),
      lastActive: new Date(),
    })
  );
  await assertSucceeds(aliceDb.doc(`users/${ALICE_UID}`).get());
});

test("user cannot read or write another user profile", async () => {
  const bobDb = testEnv.authenticatedContext(BOB_UID).firestore();
  await assertFails(bobDb.doc(`users/${ALICE_UID}`).get());
  await assertFails(
    bobDb.doc(`users/${ALICE_UID}`).set({
      uid: ALICE_UID,
      email: "hacked@example.com",
      displayName: "Attacker",
      createdAt: new Date(),
      lastActive: new Date(),
    })
  );
});

test("user can create and read valid video project", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  await assertSucceeds(
    aliceDb.doc(`users/${ALICE_UID}/projects/proj_1`).set({
      id: "proj_1",
      userId: ALICE_UID,
      name: "My Cyberpunk Reel",
      aspectRatio: "REELS_9_16",
      resolution: "1080p",
      fps: 30,
      clipsCount: 3,
      durationSeconds: 15.5,
      thumbnailUri: "content://media/1",
      createdAt: new Date(),
      updatedAt: new Date(),
    })
  );
  await assertSucceeds(aliceDb.doc(`users/${ALICE_UID}/projects/proj_1`).get());
});

test("user cannot create project with mismatched userId or invalid data", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  // Mismatched userId
  await assertFails(
    aliceDb.doc(`users/${ALICE_UID}/projects/proj_bad`).set({
      id: "proj_bad",
      userId: BOB_UID,
      name: "Spoofed Project",
      aspectRatio: "REELS_9_16",
      resolution: "1080p",
      fps: 30,
      clipsCount: 1,
      durationSeconds: 10,
      thumbnailUri: "",
      createdAt: new Date(),
      updatedAt: new Date(),
    })
  );
  // Negative clips count
  await assertFails(
    aliceDb.doc(`users/${ALICE_UID}/projects/proj_bad2`).set({
      id: "proj_bad2",
      userId: ALICE_UID,
      name: "Bad Count",
      aspectRatio: "REELS_9_16",
      resolution: "1080p",
      fps: 30,
      clipsCount: -1,
      durationSeconds: 10,
      thumbnailUri: "",
      createdAt: new Date(),
      updatedAt: new Date(),
    })
  );
});

test("bob cannot read, update, or delete alice projects", async () => {
  // Alice sets up her project via testEnv with Alice auth
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  await aliceDb.doc(`users/${ALICE_UID}/projects/proj_1`).set({
    id: "proj_1",
    userId: ALICE_UID,
    name: "Alice Secret Project",
    aspectRatio: "REELS_9_16",
    resolution: "1080p",
    fps: 30,
    clipsCount: 2,
    durationSeconds: 12,
    thumbnailUri: "content://media/2",
    createdAt: new Date(),
    updatedAt: new Date(),
  });

  const bobDb = testEnv.authenticatedContext(BOB_UID).firestore();
  await assertFails(bobDb.doc(`users/${ALICE_UID}/projects/proj_1`).get());
  await assertFails(
    bobDb.doc(`users/${ALICE_UID}/projects/proj_1`).update({
      name: "Hacked Project",
    })
  );
  await assertFails(bobDb.doc(`users/${ALICE_UID}/projects/proj_1`).delete());
});
