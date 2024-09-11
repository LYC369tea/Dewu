<template>
  <div>
    <input type="file" @change="onFileSelected" accept="image/*" />
    <div v-if="imageSrc">
      <img :src="imageSrc" alt="Selected Image" />
    </div>
    <button @click="recognizeText" :disabled="!imageSrc">Recognize Text</button>
    <div v-if="recognizedText">
      <h3>Recognized Text:</h3>
      <pre>{{ recognizedText }}</pre>
    </div>
  </div>
</template>

<script>
import { ref } from 'vue';
import { createWorker } from 'tesseract.js';

export default {
  setup() {
    const imageSrc = ref(null);
    const recognizedText = ref('');

    const onFileSelected = (event) => {
      const file = event.target.files[0];
      imageSrc.value = URL.createObjectURL(file);
    };

    const recognizeText = async () => {
      if (!imageSrc.value) return;

      const worker = createWorker({
        logger: m => console.log(m)
      });

      try {
        await worker.load();
        await worker.loadLanguage('eng');
        await worker.initialize('eng');
        const { data: { text } } = await worker.recognize(imageSrc.value);
        recognizedText.value = text;
      } catch (error) {
        console.error('Error during text recognition:', error);
        recognizedText.value = 'Error occurred during text recognition.';
      } finally {
        await worker.terminate();
      }
    };

    return {
      imageSrc,
      recognizedText,
      onFileSelected,
      recognizeText,
    };
  },
};
</script>
