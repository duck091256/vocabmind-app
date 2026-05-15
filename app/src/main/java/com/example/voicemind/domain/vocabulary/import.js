const admin = require('firebase-admin');
const serviceAccount = require('./serviceAccountKey.json');

admin.initializeApp({
  credential: admin.credential.cert(serviceAccount)
});

const db = admin.firestore();
const data = require('./vocabulary.json');

async function importData() {
  let batch = db.batch();
  let count = 0;
  for (const item of data.vocabulary) {
    const docRef = db.collection('vocabulary').doc();
    batch.set(docRef, item);
    count++;
    // commit mỗi 500 từ để tránh timeout
    if (count % 500 === 0) {
      await batch.commit();
      batch = db.batch();
      console.log(`Đã commit ${count} từ...`);
    }
  }
  await batch.commit();
  console.log(`✅ Import thành công ${count} từ vựng!`);
}

importData().catch(console.error);