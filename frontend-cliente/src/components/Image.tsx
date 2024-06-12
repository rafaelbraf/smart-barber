import React from "react";
import { ImageProps } from "../models/ImageProps";

export const Image: React.FC<ImageProps> = ({ src, alt, height, rounded }) => {
    return (
        <div className="image-container" style={{ width: '100%' }}>
            <img 
                src={src} 
                alt={alt} 
                height={height} 
                className={`img-fluid ${rounded ? 'rounded' : ''}`} 
                style={{ height, width: '100%' }} />
        </div>
    );
}