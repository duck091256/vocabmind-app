const admin = require('firebase-admin');
const serviceAccount = require('./serviceAccountKey.json');

admin.initializeApp({
  credential: admin.credential.cert(serviceAccount)
});

const db = admin.firestore();

async function deleteCollection(collectionPath, batchSize = 500) {
  const collectionRef = db.collection(collectionPath);
  const snapshot = await collectionRef.limit(batchSize).get();
  if (snapshot.empty) return;

  let batch = db.batch();
  snapshot.docs.forEach(doc => batch.delete(doc.ref));
  await batch.commit();
  console.log(`Đã xóa ${snapshot.size} documents`);
  await deleteCollection(collectionPath, batchSize);
}

deleteCollection('vocabulary').then(() => {
  console.log('✅ Đã xóa toàn bộ collection vocabulary');
}).catch(console.error);