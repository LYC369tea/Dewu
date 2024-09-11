export const ImageToBase64Converter = async (file) => {
    const MAX_WIDTH = 512;
    const MAX_HEIGHT = 512;
    const QUALITY = 0.6;
  
    if (!file || file.size === 0) {
      throw new Error("File is empty");
    }
  
    const originalImage = await createImageBitmap(file);
    const compressedImage = compressImage(originalImage);
  
    const canvas = document.createElement('canvas');
    canvas.width = compressedImage.width;
    canvas.height = compressedImage.height;
    const ctx = canvas.getContext('2d');
    ctx.drawImage(compressedImage, 0, 0);
  
    const base64Encoded = canvas.toDataURL(file.type, QUALITY);
    
    if (!isWithinSizeLimit(base64Encoded)) {
      throw new Error('Image size exceeds the limit');
    }
  
    return base64Encoded;
  
    function compressImage(originalImage) {
      const originalWidth = originalImage.width;
      const originalHeight = originalImage.height;
  
      if (originalWidth > MAX_WIDTH || originalHeight > MAX_HEIGHT) {
        const aspectRatio = originalWidth / originalHeight;
        let newWidth, newHeight;
  
        if (aspectRatio > 1) {
          newWidth = MAX_WIDTH;
          newHeight = Math.round(MAX_WIDTH / aspectRatio);
        } else {
          newHeight = MAX_HEIGHT;
          newWidth = Math.round(MAX_HEIGHT * aspectRatio);
        }
  
        const canvas = document.createElement('canvas');
        canvas.width = newWidth;
        canvas.height = newHeight;
        const ctx = canvas.getContext('2d');
        ctx.drawImage(originalImage, 0, 0, newWidth, newHeight);
  
        return canvas;
      }
  
      return originalImage;
    }
  
    function isWithinSizeLimit(base64Image) {
      const base64Data = base64Image.split(',')[1];
      const decodedLength = atob(base64Data).length;
      const sizeLimit = 20 * 512 * 512; // 20MB in bytes
      return decodedLength <= sizeLimit;
    }
  };
  